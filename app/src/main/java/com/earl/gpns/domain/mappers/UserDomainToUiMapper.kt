package com.earl.gpns.domain.mappers

interface UserDomainToUiMapper<T> {

    fun map(
        userId: String,
        image: String,
        username: String,
        online: Int,
        lastAuth: String
    ) : T
}