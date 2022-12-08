package com.earl.gpns.data.mappers

import com.earl.gpns.data.local.RoomDb
import javax.inject.Inject

class BaseNewRoomDataToDbMapper @Inject constructor() : NewRoomDataToDbMapper<RoomDb> {

    override fun map(
        roomId: String,
        name: String,
        image: String,
        author: String,
        contact: String,
        lastMessage: String,
        lastMessageAuthor: String
    ) = RoomDb(0, roomId, image, contact, lastMessage, lastMessageAuthor, true, 1)
}