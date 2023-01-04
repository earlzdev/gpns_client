package com.earl.gpns.ui.mappers

import com.earl.gpns.domain.mappers.TripNotificationDomainToUiMapper
import com.earl.gpns.ui.models.TripNotificationUi
import javax.inject.Inject

class BaseTripNotificationDomainToUiMapper @Inject constructor() : TripNotificationDomainToUiMapper<TripNotificationUi> {

    override fun map(
        id: String,
        authorName: String,
        receiverName: String,
        authorTripRole: String,
        receiverTripRole: String,
        type: String,
        timestamp: String
    ) = TripNotificationUi.Base(id, authorName, receiverName, authorTripRole, receiverTripRole, type, timestamp)
}