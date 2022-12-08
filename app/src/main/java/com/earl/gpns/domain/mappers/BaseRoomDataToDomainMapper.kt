package com.earl.gpns.domain.mappers

import com.earl.gpns.data.mappers.RoomDataToDomainMapper
import com.earl.gpns.domain.models.RoomDomain
import javax.inject.Inject

class BaseRoomDataToDomainMapper @Inject constructor() : RoomDataToDomainMapper<RoomDomain> {

    override fun map(
        roomId: String,
        image: String,
        title: String,
        lastMessage: String,
        lastMessageAuthor: String,
        deletable: Boolean,
        unreadMsgCounter: Int
    ) = RoomDomain.Base(roomId, image, title, lastMessage, lastMessageAuthor, deletable, unreadMsgCounter)
}