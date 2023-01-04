package com.earl.gpns.ui.mappers

interface TripNotificationUiToDomainMapper<T> {

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