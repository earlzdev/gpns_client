package com.earl.gpns.ui.models

import android.widget.TextView
import com.earl.gpns.core.Same
import com.makeramen.roundedimageview.RoundedImageView

interface RoomUi : Same<RoomUi> {

    override fun same(value: RoomUi) = this == value

    fun recyclerDetails(
        icon: RoundedImageView,
        name: TextView,
        lastMsg: TextView
    )

    fun chatInfo() : ChatInfo

    fun sameId(id: String) : Boolean

    fun updateLastMessage(newMsg: String)

    fun provideLastMessage() : String

    class Base(
        private val roomId: String,
        private val image: String,
        private val title: String,
        private var lastMessage: String,
        private val lastMessageAuthor: String,
        private val deletable: Boolean
    ) : RoomUi {
        override fun recyclerDetails(icon: RoundedImageView, name: TextView, lastMsg: TextView) {
            name.text = title
            lastMsg.text = lastMessage
        }

        override fun chatInfo() = ChatInfo(roomId, title, image)

        override fun sameId(id: String) = id == roomId

        override fun updateLastMessage(newMsg: String) {
            lastMessage = newMsg
        }

        override fun provideLastMessage() = lastMessage
    }
}