package com.earl.gpns.data.mappers

interface RoomResponseToDataMapper<T> {

    fun map(
        roomId: String,
        image: String,
        title: String,
        lastMessage: String,
        lastMessageAuthor: String,
        deletable: Boolean
    ) : T
}