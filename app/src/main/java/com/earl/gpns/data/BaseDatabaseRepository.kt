package com.earl.gpns.data

import android.util.Log
import com.earl.gpns.data.localDb.GroupMessagesCountDb
import com.earl.gpns.data.localDb.GroupsDao
import com.earl.gpns.data.localDb.RoomDb
import com.earl.gpns.data.localDb.RoomsDao
import com.earl.gpns.data.mappers.*
import com.earl.gpns.data.models.GroupMessagesCounterData
import com.earl.gpns.data.models.NewRoomDtoData
import com.earl.gpns.data.models.RoomData
import com.earl.gpns.domain.mappers.NewRoomDomainToDataMapper
import com.earl.gpns.domain.models.NewRoomDtoDomain
import com.earl.gpns.domain.models.RoomDomain
import com.earl.gpns.domain.DatabaseRepository
import com.earl.gpns.domain.models.GroupMessagesCounterDomain
import javax.inject.Inject

class BaseDatabaseRepository @Inject constructor(
    private val roomsDao: RoomsDao,
    private val groupsDao: GroupsDao,
    private val newRoomDataToDbMapper: NewRoomDataToDbMapper<RoomDb>,
    private val newRoomDomainToDataMapper: NewRoomDomainToDataMapper<NewRoomDtoData>,
    private val roomDbToDataMapper: RoomDbToDataMapper<RoomData>,
    private val roomDataToDomainMapper: RoomDataToDomainMapper<RoomDomain>,
    private val groupMessagesCounterDbToDataMapper: GroupMessagesCounterDbToDataMapper<GroupMessagesCounterData>,
    private val groupMessagesCounterDataToDomainMapper: GroupMessagesCounterDataToDomainMapper<GroupMessagesCounterDomain>
) : DatabaseRepository {

    override suspend fun insertNewRoomIntoLocalDb(room: NewRoomDtoDomain) {
        roomsDao.insertRoom(room.map(newRoomDomainToDataMapper).mapToDb(newRoomDataToDbMapper))
    }

    override suspend fun fetchRoomsListFromLocalDb() = roomsDao.fetchAllRooms()
        .map { it.map(roomDbToDataMapper) }
        .map { it.map(roomDataToDomainMapper) }

    override suspend fun deleteRoomFromLocalDb(roomId: String) {
        try {
            roomsDao.deleteRoomFromDb(roomId)
        } catch (e: Exception) {
            Log.d("tag", "deleteRoomFromLocalDb: EXCEPTION -> $e")
        }
    }

    override suspend fun clearLocalDataBase() {
        roomsDao.clearDatabase()
    }

    override suspend fun fetchMessagesCounterForGroup(groupId: String): GroupMessagesCounterDomain? {
        return groupsDao.selectCounterForGroup(groupId)
            ?.map(groupMessagesCounterDbToDataMapper)
            ?.map(groupMessagesCounterDataToDomainMapper)
    }

    override suspend fun insertGroupMessagesCounter(groupId: String, counter: Int) {
        try {
            val counterDb = GroupMessagesCountDb(0, groupId, counter)
            groupsDao.insertNewCounter(counterDb)
            Log.d("tag", "insertGroupMessagesCounter: added $counter")
        } catch (e: Exception) {
            Log.d("tag", "insertGroupMessagesCounter: $e")
            e.printStackTrace()
        }
    }

    override suspend fun deleteMessagesCounterInGroup(groupId: String) {
        groupsDao.deleteMessagesCounterInGroup(groupId)
    }

    override suspend fun updateMessagesReadCounter(groupId: String, counter: Int) {
        groupsDao.updateReadMessagesCount(groupId, counter)
    }
}