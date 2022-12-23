package com.earl.gpns.data.mappers

interface CompanionFormDataToDomainMapper<T> {

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