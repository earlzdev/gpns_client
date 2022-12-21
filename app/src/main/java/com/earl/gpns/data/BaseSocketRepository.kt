package com.earl.gpns.data

import android.util.Log
import com.earl.gpns.core.SocketOperationResultListener
import com.earl.gpns.data.mappers.*
import com.earl.gpns.data.models.*
import com.earl.gpns.data.models.remote.GroupMessageRemote
import com.earl.gpns.data.models.remote.MessageRemote
import com.earl.gpns.data.models.remote.ObservingSocketModel
import com.earl.gpns.data.models.remote.requests.NewRoomRequest
import com.earl.gpns.data.models.remote.responses.RoomIdResponse
import com.earl.gpns.data.models.remote.responses.RoomResponse
import com.earl.gpns.domain.SocketsRepository
import com.earl.gpns.domain.mappers.GroupMessageDomainToDataMapper
import com.earl.gpns.domain.mappers.MessageDomainToDataMapper
import com.earl.gpns.domain.mappers.NewRoomDomainToDataMapper
import com.earl.gpns.domain.models.GroupMessageDomain
import com.earl.gpns.domain.models.MessageDomain
import com.earl.gpns.domain.models.NewLastMessageInRoomDomain
import com.earl.gpns.domain.models.RoomDomain
import com.earl.gpns.domain.webSocketActions.services.GroupMessagingSocketActionsService
import com.earl.gpns.domain.webSocketActions.services.RoomsMessagingSocketActionsService
import com.earl.gpns.domain.webSocketActions.services.RoomsObservingSocketService
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class BaseSocketRepository @Inject constructor(
    private val socketClient: HttpClient,
    private val roomResponseToDataMapper: RoomResponseToDataMapper<RoomData>,
    private val roomDataToDomainMapper: RoomDataToDomainMapper<RoomDomain>,
    private val newRoomDomainToDataMapper: NewRoomDomainToDataMapper<NewRoomDtoData>,
    private val newRoomDataToRequestMapper: NewRoomDataToRequestMapper<NewRoomRequest>,
    private val messageDomainToDataMapper: MessageDomainToDataMapper<MessageData>,
    private val messageDataToRemoteMapper: MessageDataToRemoteMapper<MessageRemote>,
    private val messageRemoteToDataMapper: MessageRemoteToDataMapper<MessageData>,
    private val messageDataToDomainMapper: MessageDataToDomainMapper<MessageDomain>,
    private val lastMsgResponseToDataMapper: NewLastMsgResponseToDataMapper<NewLastMessageInRoomData>,
    private val lastMsgDataToDomainMapper: NewLastMsgDataToDomainMapper<NewLastMessageInRoomDomain>,
    private val socketActionsParser: SocketActionsParser,
    private val groupMessageRemoteToDataMapper: GroupMessageRemoteToDataMapper<GroupMessageData>,
    private val groupMessageDataToDomainMapper: GroupMessageDataToDomainMapper<GroupMessageDomain>,
    private val groupMessageDomainToDataMapper: GroupMessageDomainToDataMapper<GroupMessageData>,
    private val groupMessageDataToRemoteMapper: GroupMessageDataToRemoteMapper<GroupMessageRemote>
) : SocketsRepository {

    private var roomsSocket: WebSocketSession? = null
    private var messagingSocket: WebSocketSession? = null
    private var groupsMessagingSocket: WebSocketSession? = null
    private var usersOnlineSocket: WebSocketSession? = null
    private var searchSocket: WebSocketSession? = null

    override suspend fun initRoomsSocket(token: String): SocketOperationResultListener<Unit> {
        return try {
            roomsSocket = socketClient.webSocketSession {
                url(WebSocketService.Endpoints.Chat.url)
                header("Authorization", "Bearer $token")
            }
            if (roomsSocket?.isActive == true) {
                SocketOperationResultListener.Success(Unit)
            } else {
                SocketOperationResultListener.Error("Couldn't establish a connection")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            SocketOperationResultListener.Error(e.localizedMessage ?: "unknown error $e")
        }
    }

    override suspend fun initRoomMessagingSocket(jwtToken: String, roomId: String) {
        return try {
            messagingSocket = socketClient.webSocketSession {
                url(WebSocketService.Endpoints.Messaging.url)
                header("Authorization", "Bearer $jwtToken")
                parameter("roomId", roomId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun initGroupsMessagingSocket(token: String, groupId: String) {
        return try {
            groupsMessagingSocket = socketClient.webSocketSession {
                url(WebSocketService.Endpoints.GroupMessaging.url)
                header("Authorization", "Bearer $token")
                parameter("groupId", groupId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun closeChatSocketSession() {
        roomsSocket?.close()
    }

    override suspend fun closeMessagingSocket() {
        messagingSocket?.close()
    }

    override suspend fun closeGroupMessagingSocket() {
        groupsMessagingSocket?.close()
    }

    override suspend fun observeRoomsSocket(roomsService: RoomsObservingSocketService): Flow<RoomDomain?> {
        return try {
            roomsSocket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: "bad msg transcription"
                    val socketModel = Json.decodeFromString<ObservingSocketModel>(json)
                    socketActionsParser.setRoomObservingService(roomsService)
                    when (socketModel.action) {
                        REMOVE_DELETED_BY_ANOTHER_USER_ROOM -> {
                            socketActionsParser.removeDeletedByAnotherUserRoomFromDb(socketModel.value)
                            return@map null
                        }
                        NEW_ROOM -> {
                            val newRoom = Json.decodeFromString<RoomResponse>(socketModel.value)
                            return@map newRoom.map(roomResponseToDataMapper).map(roomDataToDomainMapper)
                        }
                        UPDATE_LAST_MESSAGE_IN_ROOM -> {
                            socketActionsParser.updateLastMessageInRoom(socketModel.value)
                            return@map null
                        }
                        UPDATE_LAST_MESSAGE_READ_STATE -> {
                            socketActionsParser.updateLastMessageInRoomReadState(socketModel.value)
                            return@map null
                        }
                        UPDATE_USER_ONLINE_IN_ROOM -> {
                            socketActionsParser.updateUserOnlineInRoomObserving(socketModel.value)
                            return@map null
                        }
                        NEW_UPDATABLE_MESSAGE_IN_GROUP -> {
                            socketActionsParser.updateLastMessageInGroup(socketModel.value)
                            return@map null
                        }
                        MARK_AUTHORED_MESSAGES_AS_READ_IN_GROUP -> {
                            socketActionsParser.markAuthoredMessagesAsReadInGroup(socketModel.value)
                            return@map null
                        }
                        else -> {
                            // todo test and don't need
                            return@map null
                        }
                    }
                }!!
        } catch (e: Exception) {
            e.printStackTrace()
            flow {  }
        }
    }

    override suspend fun observeRoomMessagingSocket(service: RoomsMessagingSocketActionsService): Flow<MessageDomain?> {
        return try {
            messagingSocket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: "bad msg transcription"
                    Log.d("tag", "observeMessages: json -> ${json}")
                    val socketModel = Json.decodeFromString<ObservingSocketModel>(json)
                    socketActionsParser.setMessagingSocketActionsService(service)
                    Log.d("tag", "observeMessages: action -> ${socketModel.action} value -> ${socketModel.value}")
                    when(socketModel.action) {
                        NEW_MESSAGE -> {
                            val messageRemote =  Json.decodeFromString<MessageRemote>(socketModel.value)
                            return@map messageRemote.map(messageRemoteToDataMapper).mapToDomain(messageDataToDomainMapper)
                        }
                        UPDATE_USER_TYPING_MESSAGE_STATE -> {
                            socketActionsParser.updateUserTypingMessageState(socketModel.value)
                            return@map null
                        }
                        UPDATE_USER_ONLINE_STATUS_IN_CHAT -> {
                            socketActionsParser.updateUserOnlineStatusInChat(socketModel.value)
                            return@map null
                        }
                        MARK_MESSAGE_AS_READ_IN_CHAT -> {
                            socketActionsParser.markMessagesAsReadInChat()
                            return@map null
                        }
                        else -> {
                            return@map null
                        }
                    }
                }!!
        } catch(e: Exception) {
            e.printStackTrace()
            flow {  }
        }
    }

    override suspend fun observeGroupMessagingSocket(service: GroupMessagingSocketActionsService): Flow<GroupMessageDomain?> {
        return try {
            groupsMessagingSocket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: "bad msg transcription"
                    val socketModel = Json.decodeFromString<ObservingSocketModel>(json)
                    socketActionsParser.setGroupMessagingActionsService(service)
                    when(socketModel.action) {
                        NEW_MESSAGE_IN_GROUP -> {
                            val messageInGroupRemote = Json.decodeFromString<GroupMessageRemote>(socketModel.value)
                            return@map messageInGroupRemote.map(groupMessageRemoteToDataMapper).mapToDomain(groupMessageDataToDomainMapper)
                        }
                        UPDATE_TYPING_MESSAGE_STATUS_IN_GROUP -> {
                            socketActionsParser.updateTypingStatusInGroup(socketModel.value)
                            return@map null
                        }
                        MARK_MESSAGES_AS_READ_IN_GROUP -> {
                            socketActionsParser.markMessagesAsReadInGroup(socketModel.value)
                            return@map null
                        }
                        else -> return@map null
                    }
                }!!
        } catch (e: Exception) {
            e.printStackTrace()
            flow {  }
        }
    }

    override suspend fun sendMessageInRoom(message: MessageDomain, token: String) {
        try {
            val requestJson = Json.encodeToString(message
                .mapToData(messageDomainToDataMapper)
                .mapToRemote(messageDataToRemoteMapper)
            )
            messagingSocket?.send(Frame.Text(requestJson))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun sendMessageInGroup(token: String, messageDomain: GroupMessageDomain) {
        try {
            val requestJson = Json.encodeToString(messageDomain
                .mapToData(groupMessageDomainToDataMapper)
                .mapToRemote(groupMessageDataToRemoteMapper)
            )
            groupsMessagingSocket?.send(Frame.Text(requestJson))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {

        private const val REMOVE_DELETED_BY_ANOTHER_USER_ROOM = "REMOVE_DELETED_BY_ANOTHER_USER_ROOM"
        private const val NEW_ROOM = "NEW_ROOM"
        private const val UPDATE_LAST_MESSAGE_IN_ROOM = "UPDATE_LAST_MESSAGE_IN_ROOM"
        private const val UPDATE_LAST_MESSAGE_READ_STATE = "UPDATE_LAST_MESSAGE_READ_STATE"
        private const val UPDATE_USER_ONLINE_IN_ROOM = "UPDATE_USER_ONLINE_IN_ROOM"
        private const val UPDATE_USER_TYPING_MESSAGE_STATE = "UPDATE_USER_TYPING_MESSAGE_STATE"
        private const val UPDATE_USER_ONLINE_STATUS_IN_CHAT = "UPDATE_USER_ONLINE_STATUS_IN_CHAT"
        private const val MARK_MESSAGE_AS_READ_IN_CHAT = "MARK_MESSAGE_AS_READ_IN_CHAT"
        private const val NEW_MESSAGE = "NEW_MESSAGE"
        private const val NEW_MESSAGE_IN_GROUP = "NEW_MESSAGE_IN_GROUP"
        private const val NEW_UPDATABLE_MESSAGE_IN_GROUP = "NEW_UPDATABLE_MESSAGE_IN_GROUP"
        private const val UPDATE_TYPING_MESSAGE_STATUS_IN_GROUP = "UPDATE_TYPING_MESSAGE_STATUS_IN_GROUP"
        private const val MARK_MESSAGES_AS_READ_IN_GROUP = "MARK_MESSAGES_AS_READ_IN_GROUP"
        private const val MARK_AUTHORED_MESSAGES_AS_READ_IN_GROUP = "MARK_AUTHORED_MESSAGES_AS_READ_IN_GROUP"
    }
}