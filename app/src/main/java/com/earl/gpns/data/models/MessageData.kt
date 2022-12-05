package com.earl.gpns.data.models

import com.earl.gpns.data.mappers.MessageDataToDomainMapper
import com.earl.gpns.data.mappers.MessageDataToRemoteMapper

interface MessageData {

    fun <T> mapToDomain(mapper: MessageDataToDomainMapper<T>) : T

    fun <T> mapToRemote(mapper: MessageDataToRemoteMapper<T>) : T

    class Base(
        private val messageId: String,
        private val roomId: String,
        private val authorId: String,
        private val timestamp: String,
        private val messageText: String,
        private val messageData: String,
        private val read: Int
    ) : MessageData {
        override fun <T> mapToDomain(mapper: MessageDataToDomainMapper<T>) =
            mapper.map(messageId, roomId, authorId, timestamp, messageText, messageData, read)

        override fun <T> mapToRemote(mapper: MessageDataToRemoteMapper<T>) =
            mapper.map(messageId, roomId, authorId, timestamp, messageText, messageData, read)
    }
}