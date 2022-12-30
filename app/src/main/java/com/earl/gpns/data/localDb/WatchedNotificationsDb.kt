package com.earl.gpns.data.localDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WatchedNotificationsDb")
data class WatchedNotificationsDb(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "notification_id") val notificationId: String,
)
