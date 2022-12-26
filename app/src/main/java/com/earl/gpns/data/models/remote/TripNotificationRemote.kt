package com.earl.gpns.data.models.remote

import com.earl.gpns.data.mappers.TripNotificationRemoteToDataMapper
import com.earl.gpns.data.models.TripNotificationData

@kotlinx.serialization.Serializable
data class TripNotificationRemote(
    val id: String,
    val authorName: String,
    val receiverName: String,
    val authorTripRole: String,
    val receiverTripRole: String,
    val isInvite: Int,
    val timestamp: String,
) {
    fun map(mapper: TripNotificationRemoteToDataMapper<TripNotificationData>) =
        mapper.map(id, authorName, receiverName, authorTripRole, receiverTripRole, isInvite, timestamp)
}
