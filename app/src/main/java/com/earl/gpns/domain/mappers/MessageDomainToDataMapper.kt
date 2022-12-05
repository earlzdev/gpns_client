package com.earl.gpns.domain.mappers

interface MessageDomainToDataMapper<T> {

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