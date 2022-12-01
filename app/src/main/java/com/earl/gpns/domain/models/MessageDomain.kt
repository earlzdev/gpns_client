package com.earl.gpns.domain.models

import com.earl.gpns.domain.mappers.MessageDomainToDataMapper
import com.earl.gpns.domain.mappers.MessageDomainToUiMapper

interface MessageDomain {

    fun <T> mapToUi(mapper: MessageDomainToUiMapper<T>) : T

    fun <T> mapToData(mapper: MessageDomainToDataMapper<T>) : T

    class Base(
        private val messageId: String,
        private val roomId: String,
        private val authorId: String,
        private val timestamp: String,
        private val messageText: String,
        private val messageData: String
    ) : MessageDomain {

        override fun <T> mapToUi(mapper: MessageDomainToUiMapper<T>) =
            mapper.map(messageId, roomId, authorId, timestamp, messageText, messageData)

        override fun <T> mapToData(mapper: MessageDomainToDataMapper<T>) =
            mapper.map(messageId, roomId, authorId, timestamp, messageText, messageData)
    }
}