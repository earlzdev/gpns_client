package com.earl.gpns.data.models

import com.earl.gpns.data.mappers.CompanionFormDetailsDataToDomainMapper
import com.earl.gpns.domain.TripFormDetails

interface CompanionFormDetailsData : TripFormDetails {

    fun <T> mapToDomain(mapper: CompanionFormDetailsDataToDomainMapper<T>) : T

    class Base(
        private val actualTripTime: String,
        private val ableToPay: String,
        private val comment: String
    ) : CompanionFormDetailsData {
        override fun <T> mapToDomain(mapper: CompanionFormDetailsDataToDomainMapper<T>) =
            mapper.map(actualTripTime, ableToPay, comment)
    }
}