package com.earl.gpns.domain.mappers

import com.earl.gpns.domain.models.TripNotificationDomain
import com.earl.gpns.ui.mappers.TripNotificationUiToDomainMapper
import javax.inject.Inject

class BaseTripNotificationUiToDomainMapper @Inject constructor() : TripNotificationUiToDomainMapper<TripNotificationDomain> {

    override fun map(
        id: String,
        authorName: String,
        receiverName: String,
        authorTripRole: String,
        receiverTripRole: String,
        type: String,
        timestamp: String,
        active: Int
    ) = TripNotificationDomain.Base(id, authorName, receiverName, authorTripRole, receiverTripRole, type, timestamp, active)
}