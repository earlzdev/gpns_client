package com.earl.gpns.data.localDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotificationsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNewNotification(notificationsDb: NotificationsDb)

    @Query("select * from notifications")
    fun fetchAllFromNotificationsDb() : List<NotificationsDb>

    @Query("delete from notifications")
    fun clearNotificationDb()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNewWatchedNotifications(watchedId: WatchedNotificationsDb)

    @Query("delete from Watchednotificationsdb")
    fun clearWatchedNotificationsDb()

    @Query("select * from WatchedNotificationsDb")
    fun fetchAllFromWatchedNotificationId() : List<WatchedNotificationsDb>
}