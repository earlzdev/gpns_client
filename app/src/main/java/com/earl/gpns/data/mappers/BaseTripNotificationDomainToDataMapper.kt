package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.TripNotificationData
import com.earl.gpns.domain.mappers.TripNotificationDomainToDataMapper
import javax.inject.Inject

class BaseTripNotificationDomainToDataMapper @Inject constructor() : TripNotificationDomainToDataMapper<TripNotificationData> {

    override fun map(
        id: String,
        authorName: String,
        receiverName: String,
        authorTripRole: String,
        receiverTripRole: String,
        type: String,
        timestamp: String,
        active: Int
    ) = TripNotificationData.Base(id, authorName, receiverName, authorTripRole, receiverTripRole, type, timestamp, active)
}