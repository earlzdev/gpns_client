package com.earl.gpns.domain

import com.earl.gpns.domain.models.GroupMessagesCounterDomain
import com.earl.gpns.domain.models.NewRoomDtoDomain
import com.earl.gpns.domain.models.RoomDomain
import com.earl.gpns.domain.models.TripNotificationDomain

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
}