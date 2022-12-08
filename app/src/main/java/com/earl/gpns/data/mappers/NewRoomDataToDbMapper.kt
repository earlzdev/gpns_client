package com.earl.gpns.data.mappers

interface NewRoomDataToDbMapper<T> {

    fun map(
        roomId: String,
        name: String,
        image: String,
        author: String,
        contact: String,
        lastMessage: String,
        lastMessageAuthor: String
    ) : T
}