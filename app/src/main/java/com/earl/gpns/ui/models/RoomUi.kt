package com.earl.gpns.ui.models

import android.widget.TextView
import com.earl.gpns.ui.core.Same
import com.earl.gpns.ui.core.CurrentDateAndTimeGiver
import com.earl.gpns.ui.rooms.RoomsObservingSocketController
import com.makeramen.roundedimageview.RoundedImageView

interface RoomUi : Same<RoomUi> {

    override fun same(value: RoomUi) = this == value

    fun recyclerDetails(
        icon: RoundedImageView,
        name: TextView,
        lastMsg: TextView,
        unreadMsgCounter: TextView,
        lastMsgTime: TextView
    )

    fun chatInfo() : ChatInfo

    fun sameId(id: String) : Boolean

    fun updateLastMessage(lastMsg: LastMessageForUpdateInRoom)

    fun updateUnreadMsgCount()

    fun clearUnreadMsgCounter()

    fun isUnreadMsgCountEqualsNull() : Boolean

    fun messageBelongsSender() : Boolean

    fun isLastMsgRead() : Boolean

    fun showUnreadMsgIndicator()

    fun isLastMessageAuthorEqualsCurrentUser() : Boolean

    fun hideAuthorMessageUnreadIndicator()

    fun isUserOnline() : Boolean

    fun setUserOnline(online: Int, lastAuthDate: String)

    fun setRoomController(controller: RoomsObservingSocketController)

    fun provideId() : String

    class Base(
        private val roomId: String,
        private val image: String,
        private val title: String,
        private var lastMessage: String,
        private var lastMessageAuthor: String,
        private val deletable: Boolean,
        private var unreadMsgCounter: Int,
        private var isLastMsgRead: Boolean,
        private var contactOnline: Int,
        private var contactLastAuth: String,
        private var lastMsgTimestamp: String
    ) : RoomUi {

        private var control : RoomsObservingSocketController? = null

        override fun setRoomController(controller: RoomsObservingSocketController) {
            control = controller
        }

        override fun recyclerDetails(
            icon: RoundedImageView,
            name: TextView,
            lastMsg: TextView,
            unreadMsgCounter: TextView,
            lastMsgTime: TextView,
        ) {
            name.text = title
            lastMsg.text = lastMessage
            unreadMsgCounter.text = this.unreadMsgCounter.toString()
            lastMsgTime.text = CurrentDateAndTimeGiver().initDateTime(lastMsgTimestamp)
        }

        override fun chatInfo() = ChatInfo(roomId, title, image, contactOnline, contactLastAuth, lastMessageAuthor)

        override fun sameId(id: String) = id == roomId

        override fun updateLastMessage(lastMsg: LastMessageForUpdateInRoom) {
            lastMessage = lastMsg.messageText
            lastMessageAuthor = lastMsg.authorName
            lastMsgTimestamp = lastMsg.timestamp
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

        override fun isUserOnline() = contactOnline == 1

        override fun setUserOnline(online: Int, lastAuthDate: String) {
            contactOnline = online
            contactLastAuth = lastAuthDate
        }

        override fun provideId() = roomId
    }
}