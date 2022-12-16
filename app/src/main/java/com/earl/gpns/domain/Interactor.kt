package com.earl.gpns.domain

import com.earl.gpns.core.*
import com.earl.gpns.data.models.remote.requests.LoginRequest
import com.earl.gpns.data.models.remote.requests.RegisterRequest
import com.earl.gpns.domain.models.*
import com.earl.gpns.domain.webSocketActions.*
import com.earl.gpns.domain.webSocketActions.services.MessagingSocketActionsService
import com.earl.gpns.domain.webSocketActions.services.RoomsObservingSocketService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface Interactor {

    suspend fun register(registerRequest: RegisterRequest, callback: AuthResultListener)

    suspend fun login(loginRequest: LoginRequest, callback: AuthResultListener)

    suspend fun authenticate(token: String, callback: AuthResultListener)

    suspend fun fetchUserInfo(token: String) : UserDomain?

    suspend fun fetchUsers(token: String) : List<UserDomain>

    suspend fun fetchRooms(token: String) : List<RoomDomain>

    suspend fun initChatSocketSession(token: String) : SocketOperationResultListener<Unit>

    suspend fun closeChatSocketSession()

    suspend fun observeNewRooms(roomsService: RoomsObservingSocketService) : Flow<RoomDomain?>

    suspend fun addRoom(token: String, newRoomDtoDomain: NewRoomDtoDomain)

    suspend fun fetchMessagesForRoom(token: String, roomId: String) : List<MessageDomain>

    suspend fun sendMessage(message: MessageDomain, token: String)

    suspend fun observeNewMessages(service: MessagingSocketActionsService) : Flow<MessageDomain?>

    suspend fun initMessagingSocket(jwtToken: String, roomId: String)

    suspend fun closeMessagingSocket()

    suspend fun fetchRoomsListFromLocalDb() : List<RoomDomain>

    suspend fun insertRoomIntoLocalDb(room: NewRoomDtoDomain)

    suspend fun deleteRoom(token: String, roomId: String)

    suspend fun clearDatabase()

    suspend fun markMessagesAsRead(token: String, roomId: String)

    suspend fun markAuthoredMessageAsRead(token: String, roomId: String, authorName: String)

    suspend fun updateLastMsgReadState(token: String, roomId: String)

    suspend fun deleteRoomFromDb(roomId: String)

    suspend fun sendTypingMessageRequest(token: String, response: TypingMessageDtoDomain)

    class Base @Inject constructor(
        private val repository: Repository,
        private val socketRepository: SocketsRepository,
        private val localDatabaseRepository: DatabaseRepository,
    ) : Interactor {

        override suspend fun register(registerRequest: RegisterRequest, callback: AuthResultListener) {
            repository.register(registerRequest, callback)
        }

        override suspend fun login(loginRequest: LoginRequest, callback: AuthResultListener) {
            repository.login(loginRequest, callback)
        }

        override suspend fun authenticate(token: String, callback: AuthResultListener) {
            repository.authenticate(token, callback)
        }

        override suspend fun fetchUsers(token: String) = repository.fetchUsers(token)

        override suspend fun fetchRooms(token: String): List<RoomDomain> {
            val remoteList = repository.fetchRooms(token)
            val localDbList = localDatabaseRepository.fetchRoomsListFromLocalDb()
            if (remoteList.isEmpty()) {
                localDatabaseRepository.clearLocalDataBase()
            } else {
                remoteList.forEach {
                    if(!localDbList.contains(it)){
                        localDatabaseRepository.deleteRoomFromLocalDb(it.provideId())
                    }
                }
            }
            return remoteList
        }

        override suspend fun initChatSocketSession(token: String) =
            socketRepository.initChatSocketSession(token)

        override suspend fun closeChatSocketSession() {
            socketRepository.closeChatSocketSession()
        }

        override suspend fun fetchUserInfo(token: String) = repository.fetchUserInfo(token)

        override suspend fun observeNewRooms(roomsService: RoomsObservingSocketService) =
            socketRepository.observeNewRooms(roomsService)

        override suspend fun addRoom(token: String, newRoomDtoDomain: NewRoomDtoDomain) {
            repository.addNewRoom(token, newRoomDtoDomain)
        }

        override suspend fun fetchMessagesForRoom(token: String, roomId: String) =
            repository.fetchMessagesForRoom(token, roomId)

        override suspend fun sendMessage(message: MessageDomain, token: String) {
            socketRepository.sendMessage(message, token)
        }

        override suspend fun observeNewMessages(service: MessagingSocketActionsService) =
            socketRepository.observeMessages(service)

        override suspend fun initMessagingSocket(jwtToken: String, roomId: String) {
            socketRepository.initMessagingSocket(jwtToken, roomId)
        }

        override suspend fun fetchRoomsListFromLocalDb() = localDatabaseRepository.fetchRoomsListFromLocalDb()

        override suspend fun insertRoomIntoLocalDb(room: NewRoomDtoDomain) {
            localDatabaseRepository.insertNewRoomIntoLocalDb(room)
        }

        override suspend fun closeMessagingSocket() {
            socketRepository.closeMessagingSocket()
        }

        override suspend fun deleteRoom(token: String, roomId: String) {
            repository.removeRoom(token, roomId)
            localDatabaseRepository.deleteRoomFromLocalDb(roomId)
        }

        override suspend fun clearDatabase() {
            localDatabaseRepository.clearLocalDataBase()
        }

        override suspend fun markMessagesAsRead(token: String, roomId: String) {
            repository.markMessagesAsRead(token, roomId)
        }

        override suspend fun markAuthoredMessageAsRead(
            token: String,
            roomId: String,
            authorName: String
        ) {
            repository.markAuthoredMessageAsRead(token, roomId, authorName)
        }

        override suspend fun updateLastMsgReadState(token: String, roomId: String) {
            repository.updateLastMsgReadState(token, roomId)
        }

        override suspend fun deleteRoomFromDb(roomId: String) {
            localDatabaseRepository.deleteRoomFromLocalDb(roomId)
        }

        override suspend fun sendTypingMessageRequest(token: String, response: TypingMessageDtoDomain) {
            repository.sendTypingMessageRequest(token, response)
        }
    }
}