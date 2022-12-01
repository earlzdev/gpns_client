package com.earl.gpns.domain.mappers

interface NewRoomDomainToDataMapper <T> {
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