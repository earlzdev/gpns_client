package com.earl.gpns.data.mappers

interface NewRoomDataToRequestMapper <T> {

    fun map(
        roomId: String,
        name: String,
        image: String,
        author: String,
        contact: String,
        lastMessage: String,
        lastMessageAuthor: String,
        contactIsOnline: Int,
        contactLastAuth: String
    ) : T
}