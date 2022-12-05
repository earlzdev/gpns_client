package com.earl.gpns.ui.models

import android.widget.TextView
import com.earl.gpns.core.Same
import com.makeramen.roundedimageview.RoundedImageView

interface RoomUi : Same<RoomUi> {

    override fun same(value: RoomUi) = this == value

    fun recyclerDetails(
        icon: RoundedImageView,
        name: TextView,
        lastMsg: TextView,
        unreadMsgCounter: TextView
    )

    fun chatInfo() : ChatInfo

    fun sameId(id: String) : Boolean

    fun updateLastMessage(lastMsg: LastMessageForUpdate)

    fun provideLastMessage() : String

    fun updateUnreadMsgCount()

    fun clearUnreadMsgCounter()

    fun isUnreadMsgCountNull() : Boolean

    fun messageBelongsSender() : Boolean

    class Base(
        private val roomId: String,
        private val image: String,
        private val title: String,
        private var lastMessage: String,
        private var lastMessageAuthor: String,
        private val deletable: Boolean,
        private var unreadMsgCount: Int
    ) : RoomUi {
        override fun recyclerDetails(
            icon: RoundedImageView,
            name: TextView,
            lastMsg: TextView,
            unreadMsgCounter: TextView
        ) {
            name.text = title
            lastMsg.text = lastMessage
            unreadMsgCounter.text = unreadMsgCount.toString()
        }

        override fun chatInfo() = ChatInfo(roomId, title, image)

        override fun sameId(id: String) = id == roomId

        override fun updateLastMessage(lastMsg: LastMessageForUpdate) {
            lastMessage = lastMsg.messageText
            lastMessageAuthor = lastMsg.authorName

        }

        override fun provideLastMessage() = lastMessage

        override fun updateUnreadMsgCount() {
            unreadMsgCount += 1
        }

        override fun clearUnreadMsgCounter() {
            unreadMsgCount = 0
        }

        override fun isUnreadMsgCountNull() = unreadMsgCount == 0

        override fun messageBelongsSender() = lastMessageAuthor == title
    }
}