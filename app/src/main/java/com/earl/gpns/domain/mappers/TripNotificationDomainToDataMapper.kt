package com.earl.gpns.domain.mappers

interface TripNotificationDomainToDataMapper<T> {

    fun map(
        id: String,
        authorName: String,
        receiverName: String,
        authorTripRole: String,
        receiverTripRole: String,
        isInvite: Int,
        timestamp: String,
    ) : T
}