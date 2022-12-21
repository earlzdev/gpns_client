package com.earl.gpns.ui.models

import android.widget.TextView
import com.earl.gpns.core.Same
import com.earl.gpns.ui.mappers.GroupMessageUiToDomainMapper
import com.makeramen.roundedimageview.RoundedImageView
import org.w3c.dom.Text
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface GroupMessageUi : Same<GroupMessageUi> {

    fun <T> mapToDomain(mapper: GroupMessageUiToDomainMapper<T>) : T

    override fun same(value: GroupMessageUi) = this == value

    fun isAuthoredMessage(authorUsername: String) : Boolean

    fun provideAuthorName() : String

    fun provideDate() : String

    fun isMessageRead() : Boolean

    fun hideAvatar()

    fun recyclerDetailsForUser(
        message: TextView,
        time: TextView
    )

    fun recyclerDetailsForContact(
        authorUsername: TextView,
        authorIcon: RoundedImageView,
        messageTime: TextView,
        message: TextView
    )

    fun markMessageAsRead()

    fun provideMessageId() : String

    fun testProvdetext() : String

    class Base(
        private val groupId: String,
        private val messageId: String,
        private val authorName: String,
        private var authorImage: String,
        private val timestamp: String,
        private val messageText: String,
        private var read: Int
    ) : GroupMessageUi {
        override fun <T> mapToDomain(mapper: GroupMessageUiToDomainMapper<T>) =
            mapper.map(groupId, messageId, authorName, authorImage, timestamp, messageText, read)

        override fun isAuthoredMessage(authorUsername: String) = authorName == authorUsername

        override fun provideAuthorName() = authorName

        override fun provideDate(): String {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            val dateTime = LocalDateTime.parse(timestamp, formatter)
            val formatter2 = DateTimeFormatter.ofPattern("d MMMM")
            return dateTime.format(formatter2)
        }

        override fun isMessageRead() = read == 1

        override fun recyclerDetailsForUser(message: TextView, time: TextView) {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            val dateTime = LocalDateTime.parse(timestamp, formatter)
            val formatter2 = DateTimeFormatter.ofPattern("HH mm")
            message.text = messageText
            time.text = dateTime.format(formatter2)
        }

        override fun recyclerDetailsForContact(
            authorUsername: TextView,
            authorIcon: RoundedImageView,
            messageTime: TextView,
            message: TextView
        ) {
            authorUsername.text = authorName
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            val dateTime = LocalDateTime.parse(timestamp, formatter)
            val formatter2 = DateTimeFormatter.ofPattern("HH mm")
            messageTime.text = dateTime.format(formatter2)
            message.text = messageText
        }

        override fun provideMessageId() = messageId

        override fun testProvdetext() = messageText

        override fun hideAvatar() {
            authorImage = ""
        }

        override fun markMessageAsRead() {
            read = 1
        }
    }
}