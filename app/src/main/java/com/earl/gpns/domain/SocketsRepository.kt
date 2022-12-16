package com.earl.gpns.domain

import com.earl.gpns.core.*
import com.earl.gpns.domain.models.MessageDomain
import com.earl.gpns.domain.models.RoomDomain
import com.earl.gpns.domain.webSocketActions.*
import com.earl.gpns.domain.webSocketActions.services.MessagingSocketActionsService
import com.earl.gpns.domain.webSocketActions.services.RoomsObservingSocketService
import kotlinx.coroutines.flow.Flow

interface SocketsRepository {

    suspend fun observeNewRooms(roomsService: RoomsObservingSocketService) : Flow<RoomDomain?>

    suspend fun initChatSocketSession(token: String) : SocketOperationResultListener<Unit>

    suspend fun closeChatSocketSession()

    suspend fun sendMessage(message: MessageDomain, token: String)

    suspend fun observeMessages(service: MessagingSocketActionsService) : Flow<MessageDomain?>

    suspend fun initMessagingSocket(jwtToken: String, roomId: String)

    suspend fun closeMessagingSocket()
}