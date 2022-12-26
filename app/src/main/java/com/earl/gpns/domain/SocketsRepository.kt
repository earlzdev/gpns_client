package com.earl.gpns.domain

import com.earl.gpns.core.*
import com.earl.gpns.domain.models.GroupMessageDomain
import com.earl.gpns.domain.models.MessageDomain
import com.earl.gpns.domain.models.RoomDomain
import com.earl.gpns.domain.models.TripFormDomain
import com.earl.gpns.domain.webSocketActions.services.GroupMessagingSocketActionsService
import com.earl.gpns.domain.webSocketActions.services.RoomsMessagingSocketActionsService
import com.earl.gpns.domain.webSocketActions.services.RoomsObservingSocketService
import com.earl.gpns.domain.webSocketActions.services.SearchingSocketService
import kotlinx.coroutines.flow.Flow

interface SocketsRepository {

    suspend fun initRoomsSocket(token: String) : SocketOperationResultListener<Unit>

    suspend fun initRoomMessagingSocket(jwtToken: String, roomId: String)

    suspend fun initGroupsMessagingSocket(token: String, groupId: String)

    suspend fun initSearchingSocket(token: String) : Boolean

    suspend fun observeRoomsSocket(roomsService: RoomsObservingSocketService) : Flow<RoomDomain?>

    suspend fun observeRoomMessagingSocket(service: RoomsMessagingSocketActionsService) : Flow<MessageDomain?>

    suspend fun observeGroupMessagingSocket(service: GroupMessagingSocketActionsService) : Flow<GroupMessageDomain?>

    suspend fun observeSearchingFormsSocket(service: SearchingSocketService) : Flow<TripFormDomain?>

    suspend fun sendMessageInRoom(message: MessageDomain, token: String)

    suspend fun sendMessageInGroup(token: String, messageDomain: GroupMessageDomain)

    suspend fun closeChatSocketSession()

    suspend fun closeMessagingSocket()

    suspend fun closeGroupMessagingSocket()
}