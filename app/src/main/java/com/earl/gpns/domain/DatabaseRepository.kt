package com.earl.gpns.domain

import com.earl.gpns.domain.models.NewRoomDtoDomain
import com.earl.gpns.domain.models.RoomDomain

interface DatabaseRepository {

    suspend fun insertNewRoomIntoLocalDb(room: NewRoomDtoDomain)

    suspend fun fetchRoomsListFromLocalDb() : List<RoomDomain>

    suspend fun deleteRoomFromLocalDb(roomId: String)

    suspend fun clearLocalDataBase()
}