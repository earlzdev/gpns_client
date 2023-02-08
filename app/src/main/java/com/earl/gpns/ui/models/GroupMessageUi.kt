package com.earl.gpns.ui.models

import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import com.earl.gpns.ui.core.Same
import com.earl.gpns.ui.mappers.GroupMessageUiToDomainMapper
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
        messageTime: TextView,
        message: TextView,
        image: ImageView
    )

    fun markMessageAsRead()

    fun provideMessageId() : String

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
            messageTime: TextView,
            message: TextView,
            image: ImageView
        ) {
            authorUsername.text = authorName
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            val dateTime = LocalDateTime.parse(timestamp, formatter)
            val formatter2 = DateTimeFormatter.ofPattern("HH mm")
            messageTime.text = dateTime.format(formatter2)
            message.text = messageText
            if (authorImage.isNotEmpty()) {
                val bytes = Base64.decode(authorImage, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                image.setImageBitmap(bitmap)
            }
        }

        override fun provideMessageId() = messageId

        override fun hideAvatar() {
            authorImage = ""
        }

        override fun markMessageAsRead() {
            read = 1
        }
    }
}