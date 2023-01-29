package com.earl.gpns.ui.mappers

interface NewRoomUiToDomainMapper <T> {

    fun map(
        roomId: String,
        name: String,
        image: String,
        author: String,
        contact: String,
        lastMessage: String,
        lastMessageAuthor: String,
        contactIsOnline: Int,
        contactLastAuth: String,
        lastMsgTimestamp: String
    ) : T
}