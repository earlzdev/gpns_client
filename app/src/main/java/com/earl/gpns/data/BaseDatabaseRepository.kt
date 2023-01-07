package com.earl.gpns.data

import android.util.Log
import com.earl.gpns.data.localDb.*
import com.earl.gpns.data.mappers.*
import com.earl.gpns.data.models.GroupMessagesCounterData
import com.earl.gpns.data.models.NewRoomDtoData
import com.earl.gpns.data.models.RoomData
import com.earl.gpns.data.models.TripNotificationData
import com.earl.gpns.domain.DatabaseRepository
import com.earl.gpns.domain.mappers.NewRoomDomainToDataMapper
import com.earl.gpns.domain.mappers.TripNotificationDomainToDataMapper
import com.earl.gpns.domain.models.GroupMessagesCounterDomain
import com.earl.gpns.domain.models.NewRoomDtoDomain
import com.earl.gpns.domain.models.RoomDomain
import com.earl.gpns.domain.models.TripNotificationDomain
import javax.inject.Inject

class BaseDatabaseRepository @Inject constructor(
    private val roomsDao: RoomsDao,
    private val groupsDao: GroupsDao,
    private val notificationsDao: NotificationsDao,
    private val newRoomDataToDbMapper: NewRoomDataToDbMapper<RoomDb>,
    private val newRoomDomainToDataMapper: NewRoomDomainToDataMapper<NewRoomDtoData>,
    private val roomDbToDataMapper: RoomDbToDataMapper<RoomData>,
    private val roomDataToDomainMapper: RoomDataToDomainMapper<RoomDomain>,
    private val groupMessagesCounterDbToDataMapper: GroupMessagesCounterDbToDataMapper<GroupMessagesCounterData>,
    private val groupMessagesCounterDataToDomainMapper: GroupMessagesCounterDataToDomainMapper<GroupMessagesCounterDomain>,
    private val notificationsDbToDataMapper: NotificationDbToDataMapper<TripNotificationData>,
    private val notificationDataToDomainMapper: TripNotificationDataToDomainMapper<TripNotificationDomain>,
    private val notificationDomainToDataMapper: TripNotificationDomainToDataMapper<TripNotificationData>,
    private val notificationDataToDbMapper: TripNotificationDataToDbMapper<NotificationsDb>
) : DatabaseRepository {

    override suspend fun insertNewRoomIntoLocalDb(room: NewRoomDtoDomain) {
        try {
            roomsDao.insertRoom(room.map(newRoomDomainToDataMapper).mapToDb(newRoomDataToDbMapper))
            val list = roomsDao.fetchAllRooms()
            Log.d("tag", "insertNewRoomIntoLocalDb: successsful")
            Log.d("tag", "insertNewRoomIntoLocalDb: room list -> $list")
        } catch (e: Exception) {
            Log.d("tag", "insertNewRoomIntoLocalDb: $e")
            e.printStackTrace()
        }
    }

    override suspend fun fetchRoomsListFromLocalDb() = roomsDao.fetchAllRooms()
        .map { it.map(roomDbToDataMapper) }
        .map { it.map(roomDataToDomainMapper) }

    override suspend fun deleteRoomFromLocalDb(roomId: String) {
        try {
            roomsDao.deleteRoomFromDb(roomId)
            Log.d("tag", "deleted room ")
        } catch (e: Exception) {
            Log.d("tag", "deleteRoomFromLocalDb: EXCEPTION -> $e")
        }
    }

    override suspend fun clearLocalDataBase() {
        Log.d("tag", "cleared db")
        roomsDao.clearDatabase()
    }

    override suspend fun fetchMessagesCounterForGroup(groupId: String): GroupMessagesCounterDomain? {
        Log.d("tag", "fetched all rooms")
        return groupsDao.selectCounterForGroup(groupId)
            ?.map(groupMessagesCounterDbToDataMapper)
            ?.map(groupMessagesCounterDataToDomainMapper)
    }

    override suspend fun fetchAllTripNotificationsFromLocalDb() =
        notificationsDao.fetchAllFromNotificationsDb()
            .map { it.map(notificationsDbToDataMapper) }
            .map { it.mapToDomain(notificationDataToDomainMapper) }


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
        Log.d("tag", "updated messages cunet in group")
        groupsDao.deleteMessagesCounterInGroup(groupId)
    }

    override suspend fun updateMessagesReadCounter(groupId: String, counter: Int) {
        Log.d("tag", "updateMessagesReadCounter")
        groupsDao.updateReadMessagesCount(groupId, counter)
    }

    override suspend fun insertNotificationIntoDb(notificationDomain: TripNotificationDomain) {
        Log.d("tag", "insertNotificationIntoDb repository")
        notificationsDao.insertNewNotification(
            notificationDomain
                .mapToData(notificationDomainToDataMapper)
                .mapToDb(notificationDataToDbMapper)
        )
    }

    override suspend fun clearNotificationsDb() {
        notificationsDao.clearNotificationDb()
    }

    override suspend fun insertNewWatchedNotificationId(id: String) {
        notificationsDao.insertNewWatchedNotifications(WatchedNotificationsDb(0, id))
    }

    override suspend fun clearWatchedNotificationsDb() {
        notificationsDao.clearWatchedNotificationsDb()
    }

    override suspend fun fetchAllWatchedNotificationsIds() =
        notificationsDao.fetchAllFromWatchedNotificationId().map { it.notificationId }

    override suspend fun fetchTripNotification(id: String) =
        notificationsDao.fetchTripNotification(id)
            .map(notificationsDbToDataMapper)
            .mapToDomain(notificationDataToDomainMapper)

    override suspend fun markNotificationAsNotActive(id: String) {
        notificationsDao.markNotificationAsNotActive(id)
    }
}