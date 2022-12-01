package com.earl.gpns.domain.mappers

interface MessageDomainToUiMapper<T> {

    fun map(
        messageId: String,
        roomId: String,
        authorId: String,
        timestamp: String,
        messageText: String,
        messageDate: String
    ) : T
}