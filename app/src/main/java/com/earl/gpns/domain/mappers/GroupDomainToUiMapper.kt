package com.earl.gpns.domain.mappers

interface GroupDomainToUiMapper <T> {

    fun map(
        groupId: String,
        title: String,
        image: String,
        lastMessage: String,
        lastMessageAuthor: String,
        lastMessageAuthorImage: String,
        lastMessageTimestamp: String,
        companionGroup: Int,
        messagesCount: Int,
        lastMsgRead: Int
    ) : T
}