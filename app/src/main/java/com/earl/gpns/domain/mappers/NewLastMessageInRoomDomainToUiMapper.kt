package com.earl.gpns.domain.mappers

interface NewLastMessageInRoomDomainToUiMapper<T> {

    fun map(
        roomId: String,
        authorName: String,
        authorImage: String,
        timestamp: String,
        messageText: String,
        read: Int
    ) : T
}