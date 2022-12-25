package com.earl.gpns.data.mappers

interface CompanionTripFormDetailsRemoteToDataMapper<T> {

    fun map(
        actualTripTime: String,
        ableToPay: String,
        comment: String
    ) : T
}