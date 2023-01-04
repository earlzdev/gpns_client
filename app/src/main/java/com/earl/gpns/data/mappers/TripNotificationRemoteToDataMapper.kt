package com.earl.gpns.data.mappers

interface TripNotificationRemoteToDataMapper<T> {

    fun map(
        id: String,
        authorName: String,
        receiverName: String,
        authorTripRole: String,
        receiverTripRole: String,
        type: String,
        timestamp: String,
        active: Int
    ) : T
}