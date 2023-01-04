package com.earl.gpns.domain.models

import com.earl.gpns.domain.mappers.TripNotificationDomainToDataMapper
import com.earl.gpns.domain.mappers.TripNotificationDomainToUiMapper

interface TripNotificationDomain {

    fun <T> mapToData(mapper: TripNotificationDomainToDataMapper<T>) : T

    fun <T> mapToUi(mapper: TripNotificationDomainToUiMapper<T>) : T

    fun provideId() : String

    class Base(
        private val id: String,
        private val authorName: String,
        private val receiverName: String,
        private val authorTripRole: String,
        private val receiverTripRole: String,
        private val type: String,
        private val timestamp: String,
        private val active: Int
    ) : TripNotificationDomain {

        override fun <T> mapToData(mapper: TripNotificationDomainToDataMapper<T>) =
            mapper.map(id, authorName, receiverName, authorTripRole, receiverTripRole, type, timestamp, active)

        override fun <T> mapToUi(mapper: TripNotificationDomainToUiMapper<T>) =
            mapper.map(id, authorName, receiverName, authorTripRole, receiverTripRole, type, timestamp, active)

        override fun provideId() = id
    }
}