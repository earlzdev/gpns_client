package com.earl.gpns.data.models

import com.earl.gpns.data.mappers.UserDataToDomainMapper

interface UserData {

    fun <T> map(mapper: UserDataToDomainMapper<T>) : T

    class Base(
        private val userId: String,
        private val image: String,
        private val username: String,
        private val online: Int,
        private val lastAuth: String,
        private val tripRole: String
    ) : UserData {
        override fun <T> map(mapper: UserDataToDomainMapper<T>) = mapper.map(userId, image, username, online,lastAuth, tripRole)
    }
}