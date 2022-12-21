package com.earl.gpns.domain.mappers

interface GroupTypingStatusDomainToDataMapper<T> {

    fun map(
        groupId: String,
        username: String,
        status: Int
    ) : T
}