package com.earl.gpns.data.mappers

interface GroupMessageRemoteToDataMapper<T> {

    fun map(
        groupId: String,
        messageId: String,
        authorName: String,
        authorImage: String,
        timestamp: String,
        messageText: String,
        read: Int
    ) : T
}