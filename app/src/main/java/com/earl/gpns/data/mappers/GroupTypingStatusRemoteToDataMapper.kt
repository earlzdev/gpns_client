package com.earl.gpns.data.mappers

interface GroupTypingStatusRemoteToDataMapper<T> {

    fun map(
        groupId: String,
        username: String,
        status: Int
    ) : T
}