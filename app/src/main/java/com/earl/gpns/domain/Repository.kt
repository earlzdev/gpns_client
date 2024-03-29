package com.earl.gpns.domain

import com.earl.gpns.data.models.remote.requests.LoginRequest
import com.earl.gpns.data.models.remote.requests.RegisterRequest
import com.earl.gpns.domain.models.*

interface Repository {

    suspend fun register(registerRequest: RegisterRequest, callback: AuthResultListener)

    suspend fun login(loginRequest: LoginRequest, callback: AuthResultListener)

    suspend fun authenticate(token: String, callback: AuthResultListener)

    suspend fun fetchUsers(token: String) : List<UserDomain>

    suspend fun fetchRooms(token: String) : List<RoomDomain>

    suspend fun fetchUserInfo(token: String) : UserDomain?

    suspend fun fetchMessagesForRoom(token: String, roomId: String) : List<MessageDomain>

    suspend fun removeRoom(token: String, roomId: String)

    suspend fun markMessagesAsRead(token: String, roomId: String)

    suspend fun markAuthoredMessageAsRead(token: String, roomId: String, authorName: String)

    suspend fun updateLastMsgReadState(token: String, roomId: String)

    suspend fun sendTypingMessageRequest(token: String, request: TypingMessageDtoDomain)

    suspend fun addNewRoom(token: String, newRoomRequest: NewRoomDtoDomain)

    suspend fun fetchGroups(token: String) : List<GroupDomain>

    suspend fun fetchMessagesForGroup(token: String, groupId: String) : List<GroupMessageDomain>

    suspend fun sendTypingMessageStatusInGroup(token: String, request: GroupTypingStatusDomain)

    suspend fun markMessagesAsReadInGroup(token: String, groupId: String)

    suspend fun sendNewDriverForm(token: String, driverForm: DriverFormDomain)

    suspend fun sendNewCompanionForm(token: String, companionForm: CompanionFormDomain)

    suspend fun fetchAllTripForms(token: String) : List<TripFormDomain>

    suspend fun inviteDriver(token: String, notification: TripNotificationDomain)

    suspend fun inviteCompanion(token: String, notification: TripNotificationDomain)

    suspend fun fetchAllTripNotifications(token: String) : List<TripNotificationDomain>

    suspend fun fetchCompanionForm(token: String, username: String) : CompanionFormDomain?

    suspend fun fetchDriverForm(token: String, username: String) : DriverFormDomain?

    suspend fun deleteDriverForm(token: String)

    suspend fun deleteCompanionForm(token: String)

    suspend fun acceptDriverToRideTogether(token: String, driverUsername: String)

    suspend fun denyDriverToRideTogether(token: String, driverUsername: String)

    suspend fun acceptCompanionToRideTogether(token: String, companionUsername: String)

    suspend fun denyCompanionToRideTogether(token: String, companionUsername: String)

    suspend fun fetchAllCompanionsInGroup(token: String, groupId: String) : List<UserDomain>

    suspend fun removeCompanionFromGroup(token: String, groupId: String, username: String)

    suspend fun leaveFromCompanionGroup(token: String, groupId: String)

    suspend fun markTripNotificationAsNotActive(token: String, notificationId: String)

    suspend fun updateUserAvatar(token: String, newImageString: String)
}