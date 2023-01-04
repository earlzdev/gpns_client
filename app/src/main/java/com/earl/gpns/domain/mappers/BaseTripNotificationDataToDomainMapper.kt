package com.earl.gpns.domain.mappers

import com.earl.gpns.data.mappers.TripNotificationDataToDomainMapper
import com.earl.gpns.domain.models.TripNotificationDomain
import javax.inject.Inject

class BaseTripNotificationDataToDomainMapper @Inject constructor(): TripNotificationDataToDomainMapper<TripNotificationDomain> {

    override fun map(
        id: String,
        authorName: String,
        receiverName: String,
        authorTripRole: String,
        receiverTripRole: String,
        type: String,
        timestamp: String
    ) = TripNotificationDomain.Base(id, authorName, receiverName, authorTripRole, receiverTripRole, type, timestamp)
}