package com.earl.gpns.domain.mappers

interface CompanionFormDetailsDomainToUiMapper<T> {

    fun map(
        actualTripTime: String,
        ableToPay: String,
        comment: String
    ) : T
}