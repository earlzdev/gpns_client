package com.earl.gpns.data

import android.util.Log
import com.earl.gpns.data.mappers.NewLastMsgDataToDomainMapper
import com.earl.gpns.data.mappers.NewLastMsgResponseToDataMapper
import com.earl.gpns.data.models.NewLastMessageInRoomData
import com.earl.gpns.data.models.remote.responses.*
import com.earl.gpns.domain.models.NewLastMessageInRoomDomain
import com.earl.gpns.domain.webSocketActions.services.MessagingSocketActionsService
import com.earl.gpns.domain.webSocketActions.services.RoomsObservingSocketService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SocketActionsParser @Inject constructor(
    private val lastMsgResponseToDataMapper: NewLastMsgResponseToDataMapper<NewLastMessageInRoomData>,
    private val lastMsgDataToDomainMapper: NewLastMsgDataToDomainMapper<NewLastMessageInRoomDomain>
)  {

    private var roomObservingService: RoomsObservingSocketService? = null
    private var messagingSocketActionsService: MessagingSocketActionsService? = null

    fun setRoomObservingService(service: RoomsObservingSocketService) {
        roomObservingService = service
    }

    fun setMessagingSocketActionsService(service: MessagingSocketActionsService) {
        messagingSocketActionsService = service
    }

    fun removeDeletedByAnotherUserRoomFromDb(json: String) {
        Log.d("tag", "removeDeletedByAnotherUserRoomFromDb: json -> $json")
        val roomResponse = Json.decodeFromString<RoomResponse>(json)
        roomObservingService?.removeDeletedByAnotherUserRoomFromDb(roomResponse.roomId, roomResponse.title)
    }

    fun updateLastMessageInRoomReadState(json: String) {
        val markRoomsMessagesAsRead = Json.decodeFromString<RoomIdResponse>(json)
        roomObservingService?.updateLastMessageInRoomReadState(markRoomsMessagesAsRead.id)
    }

    fun updateLastMessageInRoom(json: String) {
        val newLastMessage = Json.decodeFromString<NewLastMessageInRoomResponse>(json)
        val newLastMsgDomain = newLastMessage.map(lastMsgResponseToDataMapper).map(lastMsgDataToDomainMapper)
        roomObservingService?.updateLastMessageInRoom(newLastMsgDomain)
    }

    fun updateUserOnlineInRoomObserving(json: String) {
        val updateOnline = Json.decodeFromString<SetUserOnlineInRoom>(json)
        roomObservingService?.updateUserOnlineInRoomObserving(updateOnline.roomId, updateOnline.online, updateOnline.lastAuthDate)
    }

    fun updateUserTypingMessageState(json: String) {
        val userTypingState = Json.decodeFromString<TypingMessageDtoResponse>(json)
        messagingSocketActionsService?.updateUserTypingMessageState(userTypingState.typing)
    }

    fun updateUserOnlineStatusInChat(json: String) {
        val onlineStatus = Json.decodeFromString<SetUserOnlineInMessaging>(json)
        messagingSocketActionsService?.updateUserOnlineInChat(onlineStatus.online, onlineStatus.lastAuth)
    }

    fun markMessagesAsReadInChat(json: String) {
        val messageId = Json.decodeFromString<MessageIdResponse>(json)
        messagingSocketActionsService?.markMessageAsReadInChat()
    }
}