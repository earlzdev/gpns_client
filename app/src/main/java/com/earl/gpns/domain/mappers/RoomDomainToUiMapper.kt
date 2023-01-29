package com.earl.gpns.domain.mappers

interface RoomDomainToUiMapper<T> {

    fun map(
        roomId: String,
        image: String,
        title: String,
        lastMessage: String,
        lastMessageAuthor: String,
        deletable: Boolean,
        unreadMsgCounter: Int,
        lastMsgRead: Int,
        contactIsOnline: Int,
        contactLastAuth : String,
        lastMsgTimestamp: String
    ) : T
}