package com.earl.gpns.domain

import com.earl.gpns.core.*
import com.earl.gpns.domain.models.MessageDomain
import com.earl.gpns.domain.models.NewRoomDtoDomain
import com.earl.gpns.domain.models.RoomDomain
import kotlinx.coroutines.flow.Flow

interface SocketsRepository {

    suspend fun observeNewRooms(
        updateLastMessageInRoomCallback: UpdateLastMessageInRoomCallback,
        updateLastMessageReadStateCallback: LastMessageReadStateCallback,
        removeRoomCallback: DeleteRoomCallback,
        updateUserOnlineInRoomCallback: UpdateOnlineInRoomCallback
    ) : Flow<RoomDomain?>

    suspend fun initChatSocketSession(token: String) : SocketOperationResultListener<Unit>

    suspend fun closeChatSocketSession()

    suspend fun addRoom(token: String, newRoomRequest: NewRoomDtoDomain)

    suspend fun sendMessage(message: MessageDomain, token: String)

    suspend fun observeMessages(
        markMessageAsReadCallback: MarkMessageAsReadCallback,
        setUserOnlineCallback: UpdateOnlineInChatCallback
    ) : Flow<MessageDomain?>

    suspend fun initMessagingSocket(jwtToken: String, roomId: String)

    suspend fun closeMessagingSocket()

    suspend fun updateLastMessage(message: MessageDomain, token: String)
}