package com.earl.gpns.data.mappers

interface GroupTypingStatusDataToDomainMapper<T> {

    fun map(
        groupId: String,
        username: String,
        status: Int
    ) : T
}