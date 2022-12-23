package com.earl.gpns.ui.mappers

interface CompanionFormUiToDomainMapper<T> {

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