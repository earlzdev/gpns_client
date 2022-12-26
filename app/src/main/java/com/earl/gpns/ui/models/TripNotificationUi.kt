package com.earl.gpns.ui.models

import com.earl.gpns.ui.mappers.TripNotificationUiToDomainMapper

interface TripNotificationUi {

    fun <T> mapToDomain(mapper: TripNotificationUiToDomainMapper<T>) : T

    class Base(
        private val id: String,
        private val authorName: String,
        private val receiverName: String,
        private val authorTripRole: String,
        private val receiverTripRole: String,
        private val isInvite: Int,
        private val timestamp: String,
    ) : TripNotificationUi {

        override fun <T> mapToDomain(mapper: TripNotificationUiToDomainMapper<T>) =
            mapper.map(id, authorName, receiverName, authorTripRole, receiverTripRole, isInvite, timestamp)
    }
}