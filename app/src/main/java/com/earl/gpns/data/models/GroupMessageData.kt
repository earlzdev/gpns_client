package com.earl.gpns.data.models

import com.earl.gpns.data.mappers.GroupMessageDataToDomainMapper
import com.earl.gpns.data.mappers.GroupMessageDataToRemoteMapper

interface GroupMessageData {

    fun <T> mapToDomain(mapper: GroupMessageDataToDomainMapper<T>) : T

    fun <T> mapToRemote(mapper: GroupMessageDataToRemoteMapper<T>) : T

    class Base(
        private val groupId: String,
        private val messageId: String,
        private val authorName: String,
        private val authorImage: String,
        private val timestamp: String,
        private val messageText: String,
        private val read: Int
    ) : GroupMessageData {
        override fun <T> mapToDomain(mapper: GroupMessageDataToDomainMapper<T>) =
            mapper.map(groupId, messageId, authorName, authorImage, timestamp, messageText, read)

        override fun <T> mapToRemote(mapper: GroupMessageDataToRemoteMapper<T>) =
            mapper.map(groupId, messageId, authorName, authorImage, timestamp, messageText, read)
    }
}