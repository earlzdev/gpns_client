package com.earl.gpns.data

import com.earl.gpns.data.mappers.*
import com.earl.gpns.data.models.GroupData
import com.earl.gpns.data.models.GroupLastMessageData
import com.earl.gpns.data.models.NewLastMessageInRoomData
import com.earl.gpns.data.models.TripNotificationData
import com.earl.gpns.data.models.remote.GroupRemote
import com.earl.gpns.data.models.remote.TripNotificationRemote
import com.earl.gpns.data.models.remote.requests.TypingStatusInGroupRequest
import com.earl.gpns.data.models.remote.responses.*
import com.earl.gpns.domain.models.GroupDomain
import com.earl.gpns.domain.models.GroupLastMessageDomain
import com.earl.gpns.domain.models.NewLastMessageInRoomDomain
import com.earl.gpns.domain.models.TripNotificationDomain
import com.earl.gpns.domain.webSocketActions.services.GroupMessagingSocketActionsService
import com.earl.gpns.domain.webSocketActions.services.RoomsMessagingSocketActionsService
import com.earl.gpns.domain.webSocketActions.services.RoomsObservingSocketService
import com.earl.gpns.domain.webSocketActions.services.SearchingSocketService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SocketActionsParser @Inject constructor(
    private val lastMsgResponseToDataMapper: NewLastMsgResponseToDataMapper<NewLastMessageInRoomData>,
    private val lastMsgDataToDomainMapper: NewLastMsgDataToDomainMapper<NewLastMessageInRoomDomain>,
    private val groupLastMessageResponseToDataMapper: GroupLastMessageResponseToDataMapper<GroupLastMessageData>,
    private val groupLastMessageDataToDomainMapper: GroupLastMessageDataToDomainMapper<GroupLastMessageDomain>,
    private val tripNotificationRemoteToDataMapper: TripNotificationRemoteToDataMapper<TripNotificationData>,
    private val tripNotificationDataToDomainMapper: TripNotificationDataToDomainMapper<TripNotificationDomain>,
    private val groupRemoteToDataMapper: GroupRemoteToDataMapper<GroupData>,
    private val groupDataToDomainMapper: GroupDataToDomainMapper<GroupDomain>
)  {

    private var roomObservingService: RoomsObservingSocketService? = null
    private var roomsMessagingSocketActionsService: RoomsMessagingSocketActionsService? = null
    private var groupMessagingSocketActionsService: GroupMessagingSocketActionsService? = null
    private var searchingSocketService: SearchingSocketService? = null

    fun setRoomObservingService(service: RoomsObservingSocketService) {
        roomObservingService = service
    }

    fun setMessagingSocketActionsService(service: RoomsMessagingSocketActionsService) {
        roomsMessagingSocketActionsService = service
    }

    fun setGroupMessagingActionsService(service: GroupMessagingSocketActionsService) {
        groupMessagingSocketActionsService = service
    }

    fun removeDeletedByAnotherUserRoomFromDb(json: String) {
        val roomResponse = Json.decodeFromString<RoomResponse>(json)
        roomObservingService?.removeDeletedByAnotherUserRoomFromDb(roomResponse.roomId, roomResponse.title)
    }

    fun setSearchingSocketService(service: SearchingSocketService) {
        searchingSocketService = service
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
        roomsMessagingSocketActionsService?.updateUserTypingMessageState(userTypingState.typing)
    }

    fun updateUserOnlineStatusInChat(json: String) {
        val onlineStatus = Json.decodeFromString<SetUserOnlineInMessaging>(json)
        roomsMessagingSocketActionsService?.updateUserOnlineInChat(onlineStatus.online, onlineStatus.lastAuth)
    }

    fun markMessagesAsReadInChat() {
        roomsMessagingSocketActionsService?.markMessageAsReadInChat()
    }

    fun updateLastMessageInGroup(json: String) {
        val newLastMsgInGroup = Json.decodeFromString<GroupLastMessageResponse>(json)
            .map(groupLastMessageResponseToDataMapper)
            .map(groupLastMessageDataToDomainMapper)
        roomObservingService?.updateLastMessageInGroup(newLastMsgInGroup)
    }

    fun updateTypingStatusInGroup(json: String) {
        val response = Json.decodeFromString<TypingStatusInGroupRequest>(json)
        groupMessagingSocketActionsService?.updateTypingMessageStatusInGroup(response.username, response.status)
    }

    fun markMessagesAsReadInGroup(json: String) {
        val response = Json.decodeFromString<MarkMessagesAsReadInGroupResponse>(json)
        groupMessagingSocketActionsService?.markMessagesAsReadInGroup(response.groupId)
    }

    fun markAuthoredMessagesAsReadInGroup(json: String) {
        val response = Json.decodeFromString<MarkAuthoredMessagesAsReadInGroup>(json)
        roomObservingService?.markAuthoredMessagesAsReadInGroup(response.groupId)
    }

    fun newNotification(json: String) {
        val response = Json.decodeFromString<TripNotificationRemote>(json)
        searchingSocketService?.reactOnNewNotification(response
            .map(tripNotificationRemoteToDataMapper)
            .mapToDomain(tripNotificationDataToDomainMapper)
        )
    }

    fun removeDeletedSearchingFormFromList(json: String) {
        val response = Json.decodeFromString<DeletedSearchingFormDto>(json).username
        searchingSocketService?.removeDeletedSearchingFormFromList(response)
    }

    fun addNewGroup(json: String) {
        val group = Json.decodeFromString<GroupRemote>(json)
        roomObservingService?.addNewGroup(group.map(groupRemoteToDataMapper)
            .mapToDomain(groupDataToDomainMapper))
    }

    fun removeDeletedGroup(json: String) {
        val group = Json.decodeFromString<GroupRemote>(json)
        roomObservingService?.removeDeletedGroup(group.map(groupRemoteToDataMapper)
            .mapToDomain(groupDataToDomainMapper))
    }
}