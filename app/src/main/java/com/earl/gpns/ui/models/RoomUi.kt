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

    class Base(
        val roomId: String,
        val image: String,
        val title: String,
        val lastMessage: String,
        val lastMessageAuthor: String,
        val deletable: Boolean
    ) : RoomUi {
        override fun recyclerDetails(icon: RoundedImageView, name: TextView, lastMsg: TextView) {
            name.text = title
            lastMsg.text = lastMessage
        }

        override fun chatInfo() = ChatInfo(roomId, title, image)
    }
}