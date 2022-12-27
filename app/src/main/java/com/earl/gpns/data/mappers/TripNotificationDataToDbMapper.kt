package com.earl.gpns.data.mappers

interface TripNotificationDataToDbMapper<T> {

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