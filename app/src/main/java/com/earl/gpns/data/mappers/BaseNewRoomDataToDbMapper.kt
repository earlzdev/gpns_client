package com.earl.gpns.data.mappers

import com.earl.gpns.data.localDb.RoomDb
import javax.inject.Inject

class BaseNewRoomDataToDbMapper @Inject constructor() : NewRoomDataToDbMapper<RoomDb> {

    override fun map(
        roomId: String,
        name: String,
        image: String,
        author: String,
        contact: String,
        lastMessage: String,
        lastMessageAuthor: String,
        contactIsOnline: Int,
        contactLastAuth: String,
        lastMsgTimestamp: String
    ) = RoomDb(0, roomId, image, contact, lastMessage, lastMessageAuthor, true, 1, 0, contactIsOnline, contactLastAuth, lastMsgTimestamp)
}