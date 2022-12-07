package com.earl.gpns.ui.models

import android.widget.TextView
import com.earl.gpns.core.Same
import com.earl.gpns.ui.mappers.MessageUiToDomainMapper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface MessageUi : Same<MessageUi> {

    override fun same(value: MessageUi) = this == value

    fun <T> mapToDomain(mapper: MessageUiToDomainMapper<T>) : T

    fun recyclerDetails(message: TextView, time: TextView)

    fun isAuthoredMessage(token: String) : Boolean

    fun provideMessageId() : String

    fun provideDate() : String

    fun provideAuthorId() : String

    fun isMessageRead() : Boolean

    fun markMessageAsRead()

    fun testprovidetext() : String

    fun testProvideauthroid() : String

    class Base(
        private val messageId: String,
        private val roomId: String,
        private val authorId: String,
        private val timestamp: String,
        private val messageText: String,
        private val messageData: String,
        private var read: Int
    ) : MessageUi {

        override fun <T> mapToDomain(mapper: MessageUiToDomainMapper<T>) =
            mapper.map(messageId, roomId, authorId, timestamp, messageText, messageData, read)

        override fun recyclerDetails(message: TextView, time: TextView) {
            message.text = messageText
            time.text = timestamp
        }

        override fun isAuthoredMessage(token: String) = token == authorId

        override fun provideDate(): String {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            val dateTime = LocalDateTime.parse(messageData, formatter)
            val formatter2 = DateTimeFormatter.ofPattern("d MMMM")
            return dateTime.format(formatter2)
        }

        override fun provideAuthorId() = authorId

        override fun provideMessageId() = messageId

        override fun isMessageRead() = read == 1

        override fun markMessageAsRead() {
            read = 1
        }

        override fun testprovidetext() = messageText

        override fun testProvideauthroid() = authorId
    }
}