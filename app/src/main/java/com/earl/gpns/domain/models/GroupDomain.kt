package com.earl.gpns.domain.models

import com.earl.gpns.domain.mappers.GroupDomainToUiMapper

interface GroupDomain {

    fun <T> mapToUi(mapper: GroupDomainToUiMapper<T>) : T

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
    ) : GroupDomain {
        override fun <T> mapToUi(mapper: GroupDomainToUiMapper<T>) =
            mapper.map(groupId, title, image, lastMessage, lastMessageAuthor, lastMessageAuthorImage, lastMessageTimestamp, companionGroup, messagesCount, lastMsgRead)
    }
}