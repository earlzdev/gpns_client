package com.earl.gpns.data.mappers

interface UserResponseToDataMapper<T> {

    fun map(
        userId: String,
        image: String,
        username: String,
        online: Int,
        lastAuth: String,
        tripRole: String
    ) : T
}