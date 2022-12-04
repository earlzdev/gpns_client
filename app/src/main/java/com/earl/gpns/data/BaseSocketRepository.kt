package com.earl.gpns.data

import android.util.Log
import com.earl.gpns.core.SocketOperationResultListener
import com.earl.gpns.core.UpdateLastMessageInRoomCallback
import com.earl.gpns.data.mappers.*
import com.earl.gpns.data.models.MessageData
import com.earl.gpns.data.models.NewLastMessageInRoomData
import com.earl.gpns.data.models.NewRoomDtoData
import com.earl.gpns.data.models.RoomData
import com.earl.gpns.data.models.remote.MessageRemote
import com.earl.gpns.data.models.remote.requests.ChatSocketActionRequest
import com.earl.gpns.data.models.remote.requests.NewRoomRequest
import com.earl.gpns.data.models.remote.responses.NewLastMessageInRoomResponse
import com.earl.gpns.data.models.remote.responses.RoomResponse
import com.earl.gpns.domain.mappers.MessageDomainToDataMapper
import com.earl.gpns.domain.mappers.NewRoomDomainToDataMapper
import com.earl.gpns.domain.models.MessageDomain
import com.earl.gpns.domain.models.NewLastMessageInRoomDomain
import com.earl.gpns.domain.models.NewRoomDtoDomain
import com.earl.gpns.domain.models.RoomDomain
import com.earl.gpns.domain.repositories.SocketsRepository
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
    private var usersOnlineSocket: WebSocketSession? = null

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

    override suspend fun observeNewRooms(callback: UpdateLastMessageInRoomCallback): Flow<RoomDomain?> {
        var json = ""
        return try {
            roomsSocket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    json = (it as? Frame.Text)?.readText() ?: "bad msg transcription"
                    try {
                        val roomResponse = Json.decodeFromString<RoomResponse>(json)
                        Log.d("tag", "observeNewRooms: socket repository -> success returns room $roomResponse")
                        return@map roomResponse.map(roomResponseToDataMapper).map(roomDataToDomainMapper)
                    } catch (e: Exception) {
                        val newLastMessage = Json.decodeFromString<NewLastMessageInRoomResponse>(json)
                        Log.d("tag", "observeNewRooms: socket repository -> fail $e returns newLastMsg $newLastMessage")
                        callback.update(newLastMessage.map(lastMsgResponseToDataMapper).map(lastMsgDataToDomainMapper))
                        return@map null
                    }
                }!!
        } catch(e: Exception) {
            e.printStackTrace()
            val newLastMessage = Json.decodeFromString<NewLastMessageInRoomResponse>(json)
            callback.update(newLastMessage.map(lastMsgResponseToDataMapper).map(lastMsgDataToDomainMapper))
            flow {  }
        } catch (e: Exception) {
            e.printStackTrace()
            flow {  }
        }
    }

    override suspend fun observeMessages(): Flow<MessageDomain> {
        return try {
            messagingSocket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: "bad msg transcription"
                    val messageRemote =  Json.decodeFromString<MessageRemote>(json)
                    messageRemote.map(messageRemoteToDataMapper).mapToDomain(messageDataToDomainMapper)
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
        Log.d("tag", "addRoom: socket repository socket -> $roomsSocket, new room -> $newRoomRequest")
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
            // todo
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {

        private const val ADD_ROOM_KEY = "addRoom"
    }
}