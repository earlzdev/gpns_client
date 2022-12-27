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
}