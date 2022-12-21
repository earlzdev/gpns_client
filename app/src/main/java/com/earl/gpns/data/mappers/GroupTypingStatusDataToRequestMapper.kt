package com.earl.gpns.data.mappers

interface GroupTypingStatusDataToRequestMapper<T> {

    fun map(
        groupId: String,
        username: String,
        status: Int
    ) : T
}