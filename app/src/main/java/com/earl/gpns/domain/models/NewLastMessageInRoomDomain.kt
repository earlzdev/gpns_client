package com.earl.gpns.domain.models

import com.earl.gpns.domain.mappers.NewLastMessageInRoomDomainToUiMapper

interface NewLastMessageInRoomDomain {

    fun <T> mapToUi(mapper: NewLastMessageInRoomDomainToUiMapper<T>) : T

    class Base(
        private val roomId: String,
        private val authorName: String,
        private val authorImage: String,
        private val timestamp: String,
        private val messageText: String,
        private val read: Int
    ) : NewLastMessageInRoomDomain {
        override fun <T> mapToUi(mapper: NewLastMessageInRoomDomainToUiMapper<T>) =
            mapper.map(roomId, authorName, authorImage, timestamp, messageText, read)
    }
}