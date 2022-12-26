package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.remote.TripNotificationRemote
import javax.inject.Inject

class BaseTripNotificationDataToRemoteMapper @Inject constructor() : TripNotificationDataToRemoteMapper<TripNotificationRemote> {

    override fun map(
        id: String,
        authorName: String,
        receiverName: String,
        authorTripRole: String,
        receiverTripRole: String,
        isInvite: Int,
        timestamp: String
    ) = TripNotificationRemote(id, authorName, receiverName, authorTripRole, receiverTripRole, isInvite, timestamp)
}