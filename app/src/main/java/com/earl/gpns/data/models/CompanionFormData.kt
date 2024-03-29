package com.earl.gpns.data.models

import com.earl.gpns.data.mappers.CompanionFormDataToDbMapper
import com.earl.gpns.data.mappers.CompanionFormDataToDomainMapper
import com.earl.gpns.data.mappers.CompanionFormDataToRemoteMapper

interface CompanionFormData {

    fun <T> mapToRemote(mapper: CompanionFormDataToRemoteMapper<T>) : T

    fun <T> mapToDomain(mapper: CompanionFormDataToDomainMapper<T>) : T

    fun <T> mapToDb(mapper: CompanionFormDataToDbMapper<T>) : T

    class Base(
        private val username: String,
        private val userImage: String,
        private val from: String,
        private val to: String,
        private val schedule: String,
        private val actualTripTime: String,
        private val ableToPay: String?,
        private val comment: String,
        private val active: Int
    ) : CompanionFormData {
        override fun <T> mapToRemote(mapper: CompanionFormDataToRemoteMapper<T>) =
            mapper.map(username, userImage, from, to, schedule, actualTripTime, ableToPay, comment, active)

        override fun <T> mapToDomain(mapper: CompanionFormDataToDomainMapper<T>) =
            mapper.map(username, userImage, from, to, schedule, actualTripTime, ableToPay, comment, active)

        override fun <T> mapToDb(mapper: CompanionFormDataToDbMapper<T>) =
            mapper.map(username, userImage, from, to, schedule, actualTripTime, ableToPay, comment)
    }
}