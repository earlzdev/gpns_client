package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.RoomData
import javax.inject.Inject

class BaseRoomDbToDataMapper @Inject constructor() : RoomDbToDataMapper<RoomData> {

    override fun map(
        roomId: String,
        image: String,
        title: String,
        lastMessage: String,
        lastMessageAuthor: String,
        deletable: Boolean,
        unreadMsgCounter: Int
    ) = RoomData.Base(roomId, image, title, lastMessage, lastMessageAuthor, deletable, unreadMsgCounter)
}