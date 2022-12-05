package com.earl.gpns.ui.models

import android.widget.TextView
import com.earl.gpns.core.Same
import com.earl.gpns.ui.mappers.MessageUiToDomainMapper

interface MessageUi : Same<MessageUi> {

    override fun same(value: MessageUi) = this == value

    fun <T> mapToDomain(mapper: MessageUiToDomainMapper<T>) : T

    fun recyclerDetails(message: TextView, time: TextView)

    fun isAuthoredMessage(token: String) : Boolean

//    fun confirmDate(date: String) : Boolean

    fun provideDate() : String

//    fun isUserTheSame(userId: String) : Boolean

    fun provideAuthorId() : String

    class Base(
        private val messageId: String,
        private val roomId: String,
        private val authorId: String,
        private val timestamp: String,
        private val messageText: String,
        private val messageData: String,
        private val read: Int
    ) : MessageUi {

        override fun <T> mapToDomain(mapper: MessageUiToDomainMapper<T>) =
            mapper.map(messageId, roomId, authorId, timestamp, messageText, messageData, read)

        override fun recyclerDetails(message: TextView, time: TextView) {
            message.text = messageText
            time.text = timestamp
        }

        override fun isAuthoredMessage(token: String) = token == authorId

//        override fun confirmDate(date: String) = messageData == date

        override fun provideDate() = messageData

//        override fun isUserTheSame(userId: String) = authorId == userId

        override fun provideAuthorId() = authorId
    }
}