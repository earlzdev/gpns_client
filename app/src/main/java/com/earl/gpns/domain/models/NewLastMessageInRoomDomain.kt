package com.earl.gpns.domain.models

interface NewLastMessageInRoomDomain {

    fun provideRoomId() : String

    fun provideMessageText() : String

    class Base(
        private val roomId: String,
        private val authorName: String,
        private val authorImage: String,
        private val timestamp: String,
        private val messageText: String
    ) : NewLastMessageInRoomDomain {

        override fun provideRoomId() = roomId

        override fun provideMessageText() = messageText
    }
}