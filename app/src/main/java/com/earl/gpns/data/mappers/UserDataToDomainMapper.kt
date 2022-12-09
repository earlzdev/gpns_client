package com.earl.gpns.data.mappers

interface UserDataToDomainMapper<T> {

    fun map(
        userId: String,
        image: String,
        username: String,
        online: Int,
        lastAuth: String
    ) : T
}