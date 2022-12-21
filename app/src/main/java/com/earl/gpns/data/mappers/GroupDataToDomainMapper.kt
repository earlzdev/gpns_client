package com.earl.gpns.data.mappers

interface GroupDataToDomainMapper<T> {

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