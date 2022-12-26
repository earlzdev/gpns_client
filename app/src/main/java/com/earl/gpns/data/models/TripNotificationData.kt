package com.earl.gpns.data.models

import com.earl.gpns.data.mappers.TripNotificationDataToDomainMapper
import com.earl.gpns.data.mappers.TripNotificationDataToRemoteMapper

interface TripNotificationData {

    fun <T> mapToRemote(mapper: TripNotificationDataToRemoteMapper<T>) : T

    fun <T> mapToDomain(mapper: TripNotificationDataToDomainMapper<T>) : T

    class Base(
        private val id: String,
        private val authorName: String,
        private val receiverName: String,
        private val authorTripRole: String,
        private val receiverTripRole: String,
        private val isInvite: Int,
        private val timestamp: String,
    ) : TripNotificationData {

        override fun <T> mapToRemote(mapper: TripNotificationDataToRemoteMapper<T>) =
            mapper.map(id, authorName, receiverName, authorTripRole, receiverTripRole, isInvite, timestamp)

        override fun <T> mapToDomain(mapper: TripNotificationDataToDomainMapper<T>) =
            mapper.map(id, authorName, receiverName, authorTripRole, receiverTripRole, isInvite, timestamp)
    }
}