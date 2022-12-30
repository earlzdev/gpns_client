package com.earl.gpns.data.localDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomDb::class, GroupMessagesCountDb::class, NotificationsDb::class, WatchedNotificationsDb::class], version = 7)
abstract class AppDataBase : RoomDatabase() {

    abstract fun roomsDao() : RoomsDao

    abstract fun groupsDao() : GroupsDao

    abstract fun tripNotificationsDao() : NotificationsDao
}