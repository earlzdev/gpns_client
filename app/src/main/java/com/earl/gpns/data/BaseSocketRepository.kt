package com.earl.gpns.data

import com.earl.gpns.core.*
import com.earl.gpns.data.mappers.*
import com.earl.gpns.data.models.MessageData
import com.earl.gpns.data.models.NewLastMessageInRoomData
import com.earl.gpns.data.models.NewRoomDtoData
import com.earl.gpns.data.models.RoomData
import com.earl.gpns.data.models.remote.MessageRemote
import com.earl.gpns.data.models.remote.requests.ChatSocketActionRequest
import com.earl.gpns.data.models.remote.requests.NewRoomRequest
import com.earl.gpns.data.models.remote.responses.*
import com.earl.gpns.domain.SocketsRepository
import com.earl.gpns.domain.mappers.MessageDomainToDataMapper
import com.earl.gpns.domain.mappers.NewRoomDomainToDataMapper
import com.earl.gpns.domain.models.MessageDomain
import com.earl.gpns.domain.models.NewLastMessageInRoomDomain
import com.earl.gpns.domain.models.NewRoomDtoDomain
import com.earl.gpns.domain.models.RoomDomain
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
) : SocketsRepository {

    private var roomsSocket: WebSocketSession? = null
    private var messagingSocket: WebSocketSession? = null
    private var groupsMessagingSocket: WebSocketSession? = null
    private var usersOnlineSocket: WebSocketSession? = null
    private var searchSocket: WebSocketSession? = null

    override suspend fun initChatSocketSession(token: String): SocketOperationResultListener<Unit> {
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

    override suspend fun initMessagingSocket(jwtToken: String, roomId: String) {
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

    override suspend fun closeChatSocketSession() {
        roomsSocket?.close()
    }

    override suspend fun closeMessagingSocket() {
        messagingSocket?.close()
    }

    override suspend fun observeNewRooms(
        updateLastMessageInRoomCallback: UpdateLastMessageInRoomCallback,
        updateLastMessageReadStateCallback: LastMessageReadStateCallback,
        removeRoomCallback: DeleteRoomCallback,
        updateUserOnlineInRoomCallback: UpdateOnlineInRoomCallback
    ): Flow<RoomDomain?> {
        var json = ""
        return try {
            roomsSocket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    json = (it as? Frame.Text)?.readText() ?: "bad msg transcription"
                    try {
                        val roomResponse = Json.decodeFromString<RoomResponse>(json)
                        if (roomResponse.action == REMOVE_ROOM_KEY) {
                            removeRoomCallback.removeRoom(roomResponse.roomId, roomResponse.title)
                            return@map null
                        } else {
                            return@map roomResponse.map(roomResponseToDataMapper)
                                .map(roomDataToDomainMapper)
                        }
                    } catch (e: Exception) {
                        try {
                            val newLastMessage = Json.decodeFromString<NewLastMessageInRoomResponse>(json)
                            updateLastMessageInRoomCallback.updateLastMessage(newLastMessage.map(lastMsgResponseToDataMapper).map(lastMsgDataToDomainMapper))
                            return@map null
                        } catch (e: Exception) {
                            try {
                                val updateOnline = Json.decodeFromString<SetUserOnlineInRoom>(json)
                                updateUserOnlineInRoomCallback.updateOnline(updateOnline.roomId, updateOnline.online, updateOnline.lastAuthDate)
                                return@map null
                            } catch (e: Exception) {
                                val markRoomAsRead = Json.decodeFromString<RoomIdResponse>(json)
                                updateLastMessageReadStateCallback.markAuthoredMessageAsRead(markRoomAsRead.id)
                                return@map null
                            }
                        }
                    } catch (e: Exception) {
                        return@map null
                    }
                }!!
        } catch(e: Exception) {
            e.printStackTrace()
            val newLastMessage = Json.decodeFromString<NewLastMessageInRoomResponse>(json)
            updateLastMessageInRoomCallback.updateLastMessage(newLastMessage.map(lastMsgResponseToDataMapper).map(lastMsgDataToDomainMapper))
            flow {  }
        } catch (e: Exception) {
            e.printStackTrace()
            flow {  }
        }
    }

    override suspend fun observeMessages(
        markMessageAsReadCallback: MarkMessageAsReadCallback,
        setUserOnlineCallback: UpdateOnlineInChatCallback
    ): Flow<MessageDomain?> {
        return try {
            messagingSocket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: "bad msg transcription"
                    try {
                        val messageRemote =  Json.decodeFromString<MessageRemote>(json)
                        return@map messageRemote.map(messageRemoteToDataMapper).mapToDomain(messageDataToDomainMapper)
                    } catch (e: Exception) {
                        try {
                            val updateOnline = Json.decodeFromString<SetUserOnlineInMessaging>(json)
                            setUserOnlineCallback.updateOnline(updateOnline.online, updateOnline.lastAuth)
                            return@map null
                        } catch (e: Exception) {
                            val unreadMessageId = Json.decodeFromString<MessageIdResponse>(json)
                            if (unreadMessageId.messageId != "") {
                                markMessageAsReadCallback.markAsRead()
                            }
                            return@map null
                        }
                    }
                }!!
        } catch(e: Exception) {
            e.printStackTrace()
            flow {  }
        }
    }

    override suspend fun addRoom(token: String, newRoomRequest: NewRoomDtoDomain) {
        val request = ChatSocketActionRequest(
            ADD_ROOM_KEY,
            token,
            Json.encodeToString(newRoomRequest.map(newRoomDomainToDataMapper).mapToRequest(newRoomDataToRequestMapper))
        )
        val newRoomJson = Json.encodeToString(request)
        roomsSocket?.send(Frame.Text(newRoomJson))
    }

    override suspend fun sendMessage(message: MessageDomain, token: String) {
        try {
            val requestJson = Json.encodeToString(message.mapToData(messageDomainToDataMapper).mapToRemote(messageDataToRemoteMapper))
            messagingSocket?.send(Frame.Text(requestJson))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun updateLastMessage(message: MessageDomain, token: String) {
        try {

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {

        private const val ADD_ROOM_KEY = "addRoom"
        private const val REMOVE_ROOM_KEY = "remove"
    }
}