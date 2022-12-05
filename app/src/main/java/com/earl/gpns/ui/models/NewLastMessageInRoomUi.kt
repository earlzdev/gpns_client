package com.earl.gpns.ui.models

interface NewLastMessageInRoomUi {

    fun provideRoomId() : String

    fun isMessageRead() : Boolean

    fun lastMessageForUpdate() : LastMessageForUpdate

    class Base(
        private val roomId: String,
        private val authorName: String,
        private val authorImage: String,
        private val timestamp: String,
        private val messageText: String,
        private val read: Int
    ) : NewLastMessageInRoomUi {
        override fun provideRoomId() = roomId

        override fun isMessageRead() = read == 1

        override fun lastMessageForUpdate() =
            LastMessageForUpdate(authorImage, authorName, messageText, timestamp)
    }
}