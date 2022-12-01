package com.earl.gpns.data.mappers

interface MessageDataToDomainMapper<T> {

    fun map(
        messageId: String,
        roomId: String,
        authorId: String,
        timestamp: String,
        messageText: String,
        messageDate: String
    ) : T
}