package com.earl.gpns.data.mappers

interface MessageDataToRemoteMapper<T> {
    fun map(
        messageId: String,
        roomId: String,
        authorId: String,
        timestamp: String,
        messageText: String,
        messageDate: String,
        read: Int
    ) : T
}