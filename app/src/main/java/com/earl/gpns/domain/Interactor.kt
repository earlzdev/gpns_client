package com.earl.gpns.domain

import com.earl.gpns.core.AuthResultListener
import com.earl.gpns.core.SocketOperationResultListener
import com.earl.gpns.data.models.remote.requests.LoginRequest
import com.earl.gpns.data.models.remote.requests.RegisterRequest
import com.earl.gpns.domain.models.*
import com.earl.gpns.domain.webSocketActions.services.GroupMessagingSocketActionsService
import com.earl.gpns.domain.webSocketActions.services.RoomsMessagingSocketActionsService
import com.earl.gpns.domain.webSocketActions.services.RoomsObservingSocketService
import com.earl.gpns.domain.webSocketActions.services.SearchingSocketService
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

    suspend fun observeNewMessages(service: RoomsMessagingSocketActionsService) : Flow<MessageDomain?>

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

    suspend fun fetchGroups(token: String) : List<GroupDomain>

    suspend fun initGroupMessagingSocket(token: String, groupId: String)

    suspend fun observeGroupMessaging(service: GroupMessagingSocketActionsService) : Flow<GroupMessageDomain?>

    suspend fun fetchAllMessagesInGroup(token: String, groupId: String) : List<GroupMessageDomain>

    suspend fun sendMessageInGroup(token: String, message: GroupMessageDomain)

    suspend fun closeGroupMessagingSocket()

    suspend fun sendGroupTypingMessageStatus(token: String, request: GroupTypingStatusDomain)

    suspend fun markMessagesAsReadInGroup(token: String, groupId: String)

    suspend fun fetchGroupMessagesCounter(groupId: String) : GroupMessagesCounterDomain?

    suspend fun insertNewMessagesCounterInGroup(groupId: String, counter: Int)

    suspend fun deleteMessagesCounterInGroup(groupId: String)

    suspend fun updateReadMessagesCounterInGroup(groupId: String, counter: Int)

    suspend fun sendNewDriverForm(token: String, driverFormDomain: DriverFormDomain)

    suspend fun sendNewCompanionForm(token: String, companionForm: CompanionFormDomain)

    suspend fun fetchAllTripForms(token: String) : List<TripFormDomain>

    suspend fun inviteDriver(token: String, notification: TripNotificationDomain)

    suspend fun inviteCompanion(token: String, notification: TripNotificationDomain)

    suspend fun initSearchingSocket(token: String) : Boolean

    suspend fun observeSearchingForms(service: SearchingSocketService) : Flow<TripFormDomain?>

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
            val remoteListIds = remoteList.map { it.provideId() }
            val localDbList = localDatabaseRepository.fetchRoomsListFromLocalDb()
            if (remoteList.isEmpty()) {
                localDatabaseRepository.clearLocalDataBase()
            } else {
                localDbList.forEach {
                    if (!remoteListIds.contains(it.provideId())) {
                        localDatabaseRepository.deleteRoomFromLocalDb(it.provideId())
                    }
                }
            }
            return remoteList
        }

        override suspend fun initChatSocketSession(token: String) =
            socketRepository.initRoomsSocket(token)

        override suspend fun closeChatSocketSession() {
            socketRepository.closeChatSocketSession()
        }

        override suspend fun fetchUserInfo(token: String) = repository.fetchUserInfo(token)

        override suspend fun observeNewRooms(roomsService: RoomsObservingSocketService) =
            socketRepository.observeRoomsSocket(roomsService)

        override suspend fun addRoom(token: String, newRoomDtoDomain: NewRoomDtoDomain) {
            repository.addNewRoom(token, newRoomDtoDomain)
        }

        override suspend fun fetchMessagesForRoom(token: String, roomId: String) =
            repository.fetchMessagesForRoom(token, roomId)

        override suspend fun sendMessage(message: MessageDomain, token: String) {
            socketRepository.sendMessageInRoom(message, token)
        }

        override suspend fun observeNewMessages(service: RoomsMessagingSocketActionsService) =
            socketRepository.observeRoomMessagingSocket(service)

        override suspend fun initMessagingSocket(jwtToken: String, roomId: String) {
            socketRepository.initRoomMessagingSocket(jwtToken, roomId)
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

        override suspend fun fetchGroups(token: String) = repository.fetchGroups(token)

        override suspend fun initGroupMessagingSocket(token: String, groupId: String) {
            socketRepository.initGroupsMessagingSocket(token, groupId)
        }

        override suspend fun observeGroupMessaging(service: GroupMessagingSocketActionsService) =
            socketRepository.observeGroupMessagingSocket(service)

        override suspend fun fetchAllMessagesInGroup(token: String, groupId: String) =
            repository.fetchMessagesForGroup(token, groupId)

        override suspend fun sendMessageInGroup(token: String, message: GroupMessageDomain) {
            socketRepository.sendMessageInGroup(token, message)
        }

        override suspend fun closeGroupMessagingSocket() {
            socketRepository.closeGroupMessagingSocket()
        }

        override suspend fun sendGroupTypingMessageStatus(token: String, request: GroupTypingStatusDomain) {
            repository.sendTypingMessageStatusInGroup(token, request)
        }

        override suspend fun markMessagesAsReadInGroup(token: String, groupId: String) {
            repository.markMessagesAsReadInGroup(token, groupId)
        }

        override suspend fun fetchGroupMessagesCounter(groupId: String) =
            localDatabaseRepository.fetchMessagesCounterForGroup(groupId)

        override suspend fun insertNewMessagesCounterInGroup(groupId: String, counter: Int) {
            localDatabaseRepository.insertGroupMessagesCounter(groupId, counter)
        }

        override suspend fun deleteMessagesCounterInGroup(groupId: String) {
            localDatabaseRepository.deleteMessagesCounterInGroup(groupId)
        }

        override suspend fun updateReadMessagesCounterInGroup(groupId: String, counter: Int) {
            localDatabaseRepository.updateMessagesReadCounter(groupId, counter)
        }

        override suspend fun sendNewDriverForm(token: String, driverFormDomain: DriverFormDomain) {
            repository.sendNewDriverForm(token, driverFormDomain)
        }

        override suspend fun sendNewCompanionForm(token: String, companionForm: CompanionFormDomain) {
            repository.sendNewCompanionForm(token, companionForm)
        }

        override suspend fun fetchAllTripForms(token: String) = repository.fetchAllTripForms(token)


        override suspend fun inviteDriver(token: String, notification: TripNotificationDomain) {
            repository.inviteDriver(token, notification)
        }

        override suspend fun inviteCompanion(token: String, notification: TripNotificationDomain) {
            repository.inviteCompanion(token, notification)
        }

        override suspend fun initSearchingSocket(token: String) =
            socketRepository.initSearchingSocket(token)

        override suspend fun observeSearchingForms(service: SearchingSocketService) =
            socketRepository.observeSearchingFormsSocket(service)
    }
}