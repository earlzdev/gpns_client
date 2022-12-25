package com.earl.gpns.data.mappers

interface CompanionFormDetailsDataToDomainMapper<T> {

    fun map(
        actualTripTime: String,
        ableToPay: String,
        comment: String
    ) : T
}