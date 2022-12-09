package com.earl.gpns.data.mappers

interface TypingMessageDataToResponseMapper<T> {

    fun map(
        roomId: String,
        username: String,
        typing: Int
    ) : T
}