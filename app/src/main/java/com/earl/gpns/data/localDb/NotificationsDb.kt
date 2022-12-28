package com.earl.gpns.data.localDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.earl.gpns.data.mappers.NotificationDbToDataMapper
import com.earl.gpns.data.models.TripNotificationData

@Entity(tableName = "notifications")
data class NotificationsDb(
    @PrimaryKey(autoGenerate = true) val primaryId: Int,
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "authorName") val authorName: String,
    @ColumnInfo(name = "receiverName") val receiverName: String,
    @ColumnInfo(name = "authorTripRole") val authorTripRole: String,
    @ColumnInfo(name = "receiverTripRole") val receiverTripRole: String,
    @ColumnInfo(name = "isInvite") val isInvite: Int,
    @ColumnInfo(name = "timestamp") val timestamp: String,
) {
    fun map(mapper: NotificationDbToDataMapper<TripNotificationData>) =
        mapper.map(id, authorName, receiverName, authorTripRole, receiverTripRole, isInvite, timestamp)
}
