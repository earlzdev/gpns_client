package com.earl.gpns.data.mappers

import com.earl.gpns.data.localDb.NotificationsDb
import javax.inject.Inject

class BaseTripNotificationDataToDbMapper @Inject constructor() : TripNotificationDataToDbMapper<NotificationsDb> {

    override fun map(
        id: String,
        authorName: String,
        receiverName: String,
        authorTripRole: String,
        receiverTripRole: String,
        type: String,
        timestamp: String
    ) = NotificationsDb(0, id, authorName, receiverName, authorTripRole, receiverTripRole, type, timestamp)
}