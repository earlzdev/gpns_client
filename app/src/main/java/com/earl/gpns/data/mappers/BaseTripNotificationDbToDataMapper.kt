package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.TripNotificationData
import javax.inject.Inject

class BaseTripNotificationDbToDataMapper @Inject constructor() : NotificationDbToDataMapper<TripNotificationData> {

    override fun map(
        id: String,
        authorName: String,
        receiverName: String,
        authorTripRole: String,
        receiverTripRole: String,
        isInvite: Int,
        timestamp: String
    ) = TripNotificationData.Base(
        id, authorName, receiverName, authorTripRole, receiverTripRole, isInvite, timestamp
    )
}