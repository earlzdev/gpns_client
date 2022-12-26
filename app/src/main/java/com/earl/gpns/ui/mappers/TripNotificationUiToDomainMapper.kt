package com.earl.gpns.ui.mappers

interface TripNotificationUiToDomainMapper<T> {

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