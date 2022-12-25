package com.earl.gpns.data.mappers

interface CompanionTripFormDetailsRemoteToDataMapper<T> {

    fun map(
        from: String,
        to: String,
        schedule: String,
        actualTripTime: String,
        ableToPay: String,
        comment: String
    ) : T
}