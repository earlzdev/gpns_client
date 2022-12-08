package com.earl.gpns.ui.mappers

interface RoomDomainToNewRoomDomainMapper<T> {

    fun map(
        roomId: String,
        image: String,
        title: String,
        lastMessage: String,
        lastMessageAuthor: String,
        deletable: Boolean,
    ) : T
}