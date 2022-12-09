package com.earl.gpns.data.models

import com.earl.gpns.data.mappers.TypingMessageDataToResponseMapper

interface TypingMessageDtoData {

    fun <T> map(mapper: TypingMessageDataToResponseMapper<T>) : T

    class Base(
        private val roomId: String,
        private val username: String,
        private val typing: Int
    ) : TypingMessageDtoData {
        override fun <T> map(mapper: TypingMessageDataToResponseMapper<T>) = mapper.map(roomId, username, typing)
    }
}