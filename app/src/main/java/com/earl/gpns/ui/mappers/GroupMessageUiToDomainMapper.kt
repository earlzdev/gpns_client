package com.earl.gpns.ui.mappers

interface GroupMessageUiToDomainMapper<T> {

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