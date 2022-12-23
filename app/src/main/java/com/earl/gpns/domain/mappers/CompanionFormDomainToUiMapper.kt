package com.earl.gpns.domain.mappers

interface CompanionFormDomainToUiMapper<T> {

    fun map(
        username: String,
        userImage: String,
        from: String,
        to: String,
        schedule: String,
        actualTripTime: String,
        ableToPay: String?,
        comment: String
    ) : T
}