package com.earl.gpns.domain.mappers

interface GroupTypingStatusDomainToUiMapper<T> {

    fun map(
        groupId: String,
        username: String,
        status: Int
    ) : T
}