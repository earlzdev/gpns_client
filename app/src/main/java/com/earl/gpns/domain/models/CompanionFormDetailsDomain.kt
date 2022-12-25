package com.earl.gpns.domain.models

import com.earl.gpns.domain.TripFormDetails
import com.earl.gpns.domain.mappers.CompanionFormDetailsDomainToUiMapper

interface CompanionFormDetailsDomain : TripFormDetails {

    fun <T> map(mapper: CompanionFormDetailsDomainToUiMapper<T>) : T

    class Base(
        private val from: String,
        private val to: String,
        private val schedule: String,
        private val actualTripTime: String,
        private val ableToPay: String,
        private val comment: String
    ) : CompanionFormDetailsDomain {
        override fun <T> map(mapper: CompanionFormDetailsDomainToUiMapper<T>) =
            mapper.map(from, to, schedule, actualTripTime, ableToPay, comment)
    }
}