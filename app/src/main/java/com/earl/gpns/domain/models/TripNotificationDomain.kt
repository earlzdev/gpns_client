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
        private val isInvite: Int,
        private val timestamp: String,
    ) : TripNotificationDomain {

        override fun <T> mapToData(mapper: TripNotificationDomainToDataMapper<T>) =
            mapper.map(id, authorName, receiverName, authorTripRole, receiverTripRole, isInvite, timestamp)

        override fun <T> mapToUi(mapper: TripNotificationDomainToUiMapper<T>) =
            mapper.map(id, authorName, receiverName, authorTripRole, receiverTripRole, isInvite, timestamp)

        override fun provideId() = id
    }
}