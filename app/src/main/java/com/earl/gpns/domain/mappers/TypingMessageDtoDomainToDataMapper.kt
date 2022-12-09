package com.earl.gpns.domain.mappers

interface TypingMessageDtoDomainToDataMapper<T> {

    fun map(
        roomId: String,
        username: String,
        typing: Int
    ) : T
}