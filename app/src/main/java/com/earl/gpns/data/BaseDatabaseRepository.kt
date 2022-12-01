package com.earl.gpns.data

import com.earl.gpns.data.local.RoomDb
import com.earl.gpns.data.local.RoomsDao
import com.earl.gpns.data.mappers.NewRoomDataToDbMapper
import com.earl.gpns.data.mappers.RoomDataToDomainMapper
import com.earl.gpns.data.mappers.RoomDbToDataMapper
import com.earl.gpns.data.models.NewRoomDtoData
import com.earl.gpns.data.models.RoomData
import com.earl.gpns.domain.mappers.NewRoomDomainToDataMapper
import com.earl.gpns.domain.models.NewRoomDtoDomain
import com.earl.gpns.domain.models.RoomDomain
import com.earl.gpns.domain.repositories.DatabaseRepository
import javax.inject.Inject

class BaseDatabaseRepository @Inject constructor(
    private val roomsDao: RoomsDao,
    private val newRoomDataToDbMapper: NewRoomDataToDbMapper<RoomDb>,
    private val newRoomDomainToDataMapper: NewRoomDomainToDataMapper<NewRoomDtoData>,
    private val roomDbToDataMapper: RoomDbToDataMapper<RoomData>,
    private val roomDataToDomainMapper: RoomDataToDomainMapper<RoomDomain>,
) : DatabaseRepository {

    override suspend fun insertNewRoomIntoLocalDb(room: NewRoomDtoDomain) {
        roomsDao.insertRoom(room.map(newRoomDomainToDataMapper).mapToDb(newRoomDataToDbMapper))
    }

    override suspend fun fetchRoomsListFromLocalDb() = roomsDao.fetchAllRooms()
        .map { it.map(roomDbToDataMapper) }
        .map { it.map(roomDataToDomainMapper) }

    override suspend fun deleteRoomFromLocalDb(roomId: String) {
        roomsDao.deleteRoomFromDb(roomId)
    }

    override suspend fun clearLocalDataBase() {
        roomsDao.clearDatabase()
    }
}