package com.earl.gpns.domain.mappers

interface TripNotificationDomainToUiMapper<T> {

    fun map(
        id: String,
        authorName: String,
        receiverName: String,
        authorTripRole: String,
        receiverTripRole: String,
        type: String,
        timestamp: String,
    ) : T
}