package com.earl.gpns.domain.models

import com.earl.gpns.domain.mappers.TypingMessageDtoDomainToDataMapper

interface TypingMessageDtoDomain {

    fun <T> map(mapper: TypingMessageDtoDomainToDataMapper<T>) : T

    class Base(
        private val roomId: String,
        private val username: String,
        private val typing: Int
    ) : TypingMessageDtoDomain {
        override fun <T> map(mapper: TypingMessageDtoDomainToDataMapper<T>) = mapper.map(roomId, username, typing)
    }
}