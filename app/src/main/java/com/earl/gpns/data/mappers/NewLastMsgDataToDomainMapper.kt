package com.earl.gpns.data.mappers

interface NewLastMsgDataToDomainMapper<T> {

    fun map(
        roomId: String,
        authorName: String,
        authorImage: String,
        timestamp: String,
        messageText: String,
        read: Int
    ) : T
}