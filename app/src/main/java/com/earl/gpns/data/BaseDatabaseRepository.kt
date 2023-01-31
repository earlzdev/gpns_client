package com.earl.gpns.data

import com.earl.gpns.data.localDb.*
import com.earl.gpns.data.mappers.*
import com.earl.gpns.data.models.*
import com.earl.gpns.domain.DatabaseRepository
import com.earl.gpns.domain.mappers.CompanionFormDomainToDataMapper
import com.earl.gpns.domain.mappers.DriverFormDomainToDataMapper
import com.earl.gpns.domain.mappers.NewRoomDomainToDataMapper
import com.earl.gpns.domain.mappers.TripNotificationDomainToDataMapper
import com.earl.gpns.domain.models.*
import javax.inject.Inject

class BaseDatabaseRepository @Inject constructor(
    private val roomsDao: RoomsDao,
    private val groupsDao: GroupsDao,
    private val notificationsDao: NotificationsDao,
    private val tripFormDao: TripFormDao,
    private val companionGroupUsersDao: CompanionGroupUsersDao,
    private val newRoomDataToDbMapper: NewRoomDataToDbMapper<RoomDb>,
    private val newRoomDomainToDataMapper: NewRoomDomainToDataMapper<NewRoomDtoData>,
    private val roomDbToDataMapper: RoomDbToDataMapper<RoomData>,
    private val roomDataToDomainMapper: RoomDataToDomainMapper<RoomDomain>,
    private val groupMessagesCounterDbToDataMapper: GroupMessagesCounterDbToDataMapper<GroupMessagesCounterData>,
    private val groupMessagesCounterDataToDomainMapper: GroupMessagesCounterDataToDomainMapper<GroupMessagesCounterDomain>,
    private val notificationsDbToDataMapper: NotificationDbToDataMapper<TripNotificationData>,
    private val notificationDataToDomainMapper: TripNotificationDataToDomainMapper<TripNotificationDomain>,
    private val notificationDomainToDataMapper: TripNotificationDomainToDataMapper<TripNotificationData>,
    private val notificationDataToDbMapper: TripNotificationDataToDbMapper<NotificationsDb>,
    private val companionFormDomainToDataMapper: CompanionFormDomainToDataMapper<CompanionFormData>,
    private val companionFormDataToDbMapper: CompanionFormDataToDbMapper<CompanionFormDb>,
    private val driverFormDomainToDataMapper: DriverFormDomainToDataMapper<DriverFormData>,
    private val driverFormDataToDbMapper: DriverFormDataToDbMapper<DriverFormDb>,
    private val companionFormDbToDataMapper: CompanionFormDbToDataMapper<CompanionFormData>,
    private val companionFormDataToDomainMapper: CompanionFormDataToDomainMapper<CompanionFormDomain>,
    private val driverFormDbToDataMapper: DriverFormDbToDataMapper<DriverFormData>,
    private val driverFormDataToDomainMapper: DriverFormDataToDomainMapper<DriverFormDomain>
) : DatabaseRepository {

    override suspend fun insertNewRoomIntoLocalDb(room: NewRoomDtoDomain) {
        try {
            roomsDao.insertRoom(room.map(newRoomDomainToDataMapper).mapToDb(newRoomDataToDbMapper))
            val list = roomsDao.fetchAllRooms()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun fetchRoomsListFromLocalDb() = roomsDao.fetchAllRooms()
        .map { it.map(roomDbToDataMapper) }
        .map { it.map(roomDataToDomainMapper) }

    override suspend fun deleteRoomFromLocalDb(roomId: String) {
        try {
            roomsDao.deleteRoomFromDb(roomId)
        } catch (e: Exception) {
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

    override suspend fun fetchAllTripNotificationsFromLocalDb() =
        notificationsDao.fetchAllFromNotificationsDb()
            .map { it.map(notificationsDbToDataMapper) }
            .map { it.mapToDomain(notificationDataToDomainMapper) }


    override suspend fun insertGroupMessagesCounter(groupId: String, counter: Int) {
        try {
            val counterDb = GroupMessagesCountDb(0, groupId, counter)
            groupsDao.insertNewCounter(counterDb)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteMessagesCounterInGroup(groupId: String) {
        groupsDao.deleteMessagesCounterInGroup(groupId)
    }

    override suspend fun updateMessagesReadCounter(groupId: String, counter: Int) {
        groupsDao.updateReadMessagesCount(groupId, counter)
    }

    override suspend fun insertNotificationIntoDb(notificationDomain: TripNotificationDomain) {
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

    override suspend fun insertNewUserIntoCompanionGroup(user: String) {
        companionGroupUsersDao.insertNewUserIntoCompanionGroup(CompanionGroupUser(0, user))
    }

    override suspend fun fetchAllUsernamesFromCompanionGroupFromLocalDb() =
        companionGroupUsersDao.fetchAllCompanionUsers().map { it.name }

    override suspend fun removeUserFromCompanionGroupInLocalDb(username: String) {
        companionGroupUsersDao.removeUserFromCompanionGroupInLocalDb(username)
    }

    override suspend fun clearLocalDbCompanionGroupUsersList() {
        companionGroupUsersDao.clearLocalDbCompanionGroupUsersList()
    }

    override suspend fun saveCompanionTripFormIntoLocalDb(tripForm: CompanionFormDomain) {
        tripFormDao.insertNewCompanionForm(tripForm
            .mapToData(companionFormDomainToDataMapper)
            .mapToDb(companionFormDataToDbMapper))
    }

    override suspend fun saveDriverTripFormIntoLocalDb(tripForm: DriverFormDomain) {
        tripFormDao.insertNewDriverForm(
            tripForm
                .mapToData(driverFormDomainToDataMapper)
                .mapToDb(driverFormDataToDbMapper)
        )
    }

    override suspend fun fetchCompanionTripFormFromLocalDb() =
        tripFormDao.fetchCompanionFormFromLocalDb()
            .mapToData(companionFormDbToDataMapper)
            .mapToDomain(companionFormDataToDomainMapper)


    override suspend fun fetchDriverTripFormFromLocalDb() =
        tripFormDao.fetchDriverFormFromLocalDb()
            .mapToData(driverFormDbToDataMapper)
            .mapToDomain(driverFormDataToDomainMapper)

    override suspend fun clearDriverFormInLocalDb() {
        tripFormDao.clearDriverFormDb()
    }

    override suspend fun clearCompanionFormInLocalDb() {
        tripFormDao.clearCompanionFormDb()
    }
}