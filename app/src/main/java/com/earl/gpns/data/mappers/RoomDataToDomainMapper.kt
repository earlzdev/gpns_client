package com.earl.gpns.data.mappers

interface RoomDataToDomainMapper <T> {

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
        contactLastAuth: String
    ) : T
}