package com.earl.gpns.domain.models

import com.earl.gpns.domain.mappers.UserDomainToUiMapper

interface UserDomain {

    fun <T> map(mapper: UserDomainToUiMapper<T>) : T

    class Base(
        private val userId: String,
        private val image: String,
        private val username: String,
        private val online: Int,
        private val lastAuth: String,
        private val tripRole: String
    ) : UserDomain {
        override fun <T> map(mapper: UserDomainToUiMapper<T>) = mapper.map(userId, image, username, online, lastAuth, tripRole)
    }
}