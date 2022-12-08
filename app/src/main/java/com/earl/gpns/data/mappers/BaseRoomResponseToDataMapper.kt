package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.RoomData
import javax.inject.Inject

class BaseRoomResponseToDataMapper @Inject constructor() : RoomResponseToDataMapper<RoomData> {

    override fun map(
        roomId: String,
        image: String,
        title: String,
        lastMessage: String,
        lastMessageAuthor: String,
        deletable: Boolean,
        unreadMsgCounter: Int,
        lastMsgRead: Int
    ) = RoomData.Base(roomId, image, title, lastMessage, lastMessageAuthor, deletable, unreadMsgCounter, lastMsgRead)
}