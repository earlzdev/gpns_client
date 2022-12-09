package com.earl.gpns.ui.mappers

import com.earl.gpns.domain.mappers.RoomDomainToUiMapper
import com.earl.gpns.ui.models.RoomUi
import javax.inject.Inject

class BaseRoomDomainToUiMapper @Inject constructor() : RoomDomainToUiMapper<RoomUi> {

    override fun map(
        roomId: String,
        image: String,
        title: String,
        lastMessage: String,
        lastMessageAuthor: String,
        deletable: Boolean,
        unreadMsgCounter: Int,
        lastMsgRead: Int,
        contactIsOnline: Int,
        contactLastAuth : String
    ) = RoomUi.Base(
        roomId,
        image,
        title,
        lastMessage,
        lastMessageAuthor,
        deletable,
        unreadMsgCounter,
        lastMsgRead == 1,
        contactIsOnline,
        contactLastAuth
    )
}