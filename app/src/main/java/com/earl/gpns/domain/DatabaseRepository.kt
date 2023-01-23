package com.earl.gpns.domain

import com.earl.gpns.domain.models.*

interface DatabaseRepository {

    suspend fun insertNewRoomIntoLocalDb(room: NewRoomDtoDomain)

    suspend fun fetchRoomsListFromLocalDb() : List<RoomDomain>

    suspend fun deleteRoomFromLocalDb(roomId: String)

    suspend fun fetchMessagesCounterForGroup(groupId: String) : GroupMessagesCounterDomain?

    suspend fun insertGroupMessagesCounter(groupId: String, counter: Int)

    suspend fun deleteMessagesCounterInGroup(groupId: String)

    suspend fun updateMessagesReadCounter(groupId: String, counter: Int)

    suspend fun clearLocalDataBase()

    suspend fun fetchAllTripNotificationsFromLocalDb() : List<TripNotificationDomain>

    suspend fun insertNotificationIntoDb(notificationDomain: TripNotificationDomain)

    suspend fun clearNotificationsDb()

    suspend fun insertNewWatchedNotificationId(id: String)

    suspend fun clearWatchedNotificationsDb()

    suspend fun fetchAllWatchedNotificationsIds() : List<String>

    suspend fun fetchTripNotification(id: String) : TripNotificationDomain

    suspend fun markNotificationAsNotActive(id: String)

    suspend fun insertNewUserIntoCompanionGroup(user: String)

    suspend fun fetchAllUsernamesFromCompanionGroupFromLocalDb() : List<String>

    suspend fun removeUserFromCompanionGroupInLocalDb(username: String)

    suspend fun clearLocalDbCompanionGroupUsersList()

    suspend fun saveCompanionTripFormIntoLocalDb(tripForm: CompanionFormDomain)

    suspend fun saveDriverTripFormIntoLocalDb(tripForm: DriverFormDomain)

    suspend fun fetchCompanionTripFormFromLocalDb() : CompanionFormDomain

    suspend fun fetchDriverTripFormFromLocalDb() : DriverFormDomain

    suspend fun clearDriverFormInLocalDb()

    suspend fun clearCompanionFormInLocalDb()
}