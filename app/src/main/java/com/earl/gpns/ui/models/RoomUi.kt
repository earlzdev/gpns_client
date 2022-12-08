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

    fun updateUnreadMsgCount()

    fun clearUnreadMsgCounter()

    fun isUnreadMsgCountEqualsNull() : Boolean

    fun messageBelongsSender() : Boolean

    fun isLastMsgRead() : Boolean

    fun showUnreadMsgIndicator()

    fun isLastMessageAuthorEqualsCurrentUser() : Boolean

    fun hideAuthorMessageUnreadIndicator()

    class Base(
        private val roomId: String,
        private val image: String,
        private val title: String,
        private var lastMessage: String,
        private var lastMessageAuthor: String,
        private val deletable: Boolean,
        private var unreadMsgCounter: Int,
        private var isLastMsgRead: Boolean
    ) : RoomUi {
        override fun recyclerDetails(
            icon: RoundedImageView,
            name: TextView,
            lastMsg: TextView,
            unreadMsgCounter: TextView
        ) {
            name.text = title
            lastMsg.text = lastMessage
            unreadMsgCounter.text = this.unreadMsgCounter.toString()
        }

        override fun chatInfo() = ChatInfo(roomId, title, image)

        override fun sameId(id: String) = id == roomId

        override fun updateLastMessage(lastMsg: LastMessageForUpdate) {
            lastMessage = lastMsg.messageText
            lastMessageAuthor = lastMsg.authorName

        }

        override fun updateUnreadMsgCount() {
            unreadMsgCounter += 1
        }

        override fun clearUnreadMsgCounter() {
            unreadMsgCounter = 0
        }

        override fun isUnreadMsgCountEqualsNull() = unreadMsgCounter == 0

        override fun messageBelongsSender() = lastMessageAuthor == title

        override fun isLastMsgRead() = isLastMsgRead

        override fun showUnreadMsgIndicator() {
            isLastMsgRead = false
        }

        override fun isLastMessageAuthorEqualsCurrentUser() = title != lastMessageAuthor

        override fun hideAuthorMessageUnreadIndicator() {
            isLastMsgRead = true
        }
    }
}