package com.earl.gpns.data.models

import com.earl.gpns.data.mappers.NewLastMsgDataToDomainMapper

interface NewLastMessageInRoomData {

    fun <T> map(mapper: NewLastMsgDataToDomainMapper<T>) : T

    class Base(
        private val roomId: String,
        private val authorName: String,
        private val authorImage: String,
        private val timestamp: String,
        private val messageText: String,
        private val read: Int
    ) : NewLastMessageInRoomData {
        override fun <T> map(mapper: NewLastMsgDataToDomainMapper<T>) =
            mapper.map(roomId, authorName, authorImage, timestamp, messageText, read)
    }
}