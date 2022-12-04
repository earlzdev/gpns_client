package com.earl.gpns.data.mappers

interface NewLastMsgResponseToDataMapper<T> {

    fun map(
        roomId: String,
        authorName: String,
        authorImage: String,
        timestamp: String,
        messageText: String
    ) : T
}