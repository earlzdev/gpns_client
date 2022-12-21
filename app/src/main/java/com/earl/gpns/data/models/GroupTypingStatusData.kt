package com.earl.gpns.data.models

import com.earl.gpns.data.mappers.GroupTypingStatusDataToRequestMapper

interface GroupTypingStatusData {

    fun <T> map(mapper: GroupTypingStatusDataToRequestMapper<T>) : T

    class Base(
        private val groupId: String,
        private val username: String,
        private val status: Int
    ) : GroupTypingStatusData {

        override fun <T> map(mapper: GroupTypingStatusDataToRequestMapper<T>) =
            mapper.map(groupId, username, status)
    }
}