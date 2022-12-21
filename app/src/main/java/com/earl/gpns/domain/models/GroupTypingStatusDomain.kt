package com.earl.gpns.domain.models

import com.earl.gpns.domain.mappers.GroupTypingStatusDomainToDataMapper

interface GroupTypingStatusDomain {

    fun <T> map(mapper: GroupTypingStatusDomainToDataMapper<T>) : T

    class Base(
        private val groupId: String,
        private val username: String,
        private val status: Int
    ) : GroupTypingStatusDomain {
        override fun <T> map(mapper: GroupTypingStatusDomainToDataMapper<T>) =
            mapper.map(groupId, username, status)
    }
}