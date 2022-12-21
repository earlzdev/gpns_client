package com.earl.gpns.data.models

import com.earl.gpns.data.mappers.GroupDataToDomainMapper

interface GroupData {

    fun <T> mapToDomain(mapper: GroupDataToDomainMapper<T>) : T

    class Base(
        private val groupId: String,
        private val title: String,
        private val image: String,
        private val lastMessage: String,
        private val lastMessageAuthor: String,
        private val lastMessageAuthorImage: String,
        private val lastMessageTimestamp: String,
        private val companionGroup: Int,
        private val messagesCount: Int,
        private val lastMsgRead: Int
    ) : GroupData {
        override fun <T> mapToDomain(mapper: GroupDataToDomainMapper<T>) =
            mapper.map(groupId, title, image, lastMessage, lastMessageAuthor, lastMessageAuthorImage, lastMessageTimestamp, companionGroup, messagesCount, lastMsgRead)
    }
}