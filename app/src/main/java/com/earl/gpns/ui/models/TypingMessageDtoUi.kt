package com.earl.gpns.ui.models

import com.earl.gpns.ui.mappers.TypingMessageDtoUiToDomainMapper

interface TypingMessageDtoUi {

    fun <T> map(mapper: TypingMessageDtoUiToDomainMapper<T>) : T

    class Base(
        private val roomId: String,
        private val username: String,
        private val typing: Int
    ) : TypingMessageDtoUi {
        override fun <T> map(mapper: TypingMessageDtoUiToDomainMapper<T>) = mapper.map(roomId, username, typing)
    }
}