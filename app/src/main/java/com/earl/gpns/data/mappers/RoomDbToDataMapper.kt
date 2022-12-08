package com.earl.gpns.data.mappers

interface RoomDbToDataMapper<T> {

    fun map(
        roomId: String,
        image: String,
        title: String,
        lastMessage: String,
        lastMessageAuthor: String,
        deletable: Boolean,
        unreadMsgCounter: Int,
        lastMsgRead: Int
    ) : T
}