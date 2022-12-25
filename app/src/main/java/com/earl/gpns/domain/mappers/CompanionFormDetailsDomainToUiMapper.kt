package com.earl.gpns.domain.mappers

interface CompanionFormDetailsDomainToUiMapper<T> {

    fun map(
        from: String,
        to: String,
        schedule: String,
        actualTripTime: String,
        ableToPay: String,
        comment: String
    ) : T
}