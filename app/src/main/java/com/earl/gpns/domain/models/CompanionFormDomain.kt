package com.earl.gpns.domain.models

import com.earl.gpns.domain.mappers.CompanionFormDomainToDataMapper
import com.earl.gpns.domain.mappers.CompanionFormDomainToUiMapper

interface CompanionFormDomain {

    fun <T> mapToData(mapper: CompanionFormDomainToDataMapper<T>) : T

    fun <T> mapToUi(mapper: CompanionFormDomainToUiMapper<T>) : T

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
    ) : CompanionFormDomain {
        override fun <T> mapToData(mapper: CompanionFormDomainToDataMapper<T>) =
            mapper.map(username, userImage, from, to, schedule, actualTripTime, ableToPay, comment, active)

        override fun <T> mapToUi(mapper: CompanionFormDomainToUiMapper<T>) =
            mapper.map(username, userImage, from, to, schedule, actualTripTime, ableToPay, comment, active)
    }
}