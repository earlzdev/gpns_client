package com.earl.gpns.ui.models

import com.earl.gpns.ui.mappers.GroupTypingStatusUiToDomainMapper

interface GroupTypingStatusUi {

    fun <T> map(mapper: GroupTypingStatusUiToDomainMapper<T>) : T

    class Base(
        private val groupId: String,
        private val username: String,
        private val status: Int
    ) : GroupTypingStatusUi {
        override fun <T> map(mapper: GroupTypingStatusUiToDomainMapper<T>) =
            mapper.map(groupId, username, status)
    }
}