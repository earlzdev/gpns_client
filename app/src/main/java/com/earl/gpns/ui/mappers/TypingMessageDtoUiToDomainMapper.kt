package com.earl.gpns.ui.mappers

interface TypingMessageDtoUiToDomainMapper<T> {

    fun map(
        roomId: String,
        username: String,
        typing: Int
    ) : T
}