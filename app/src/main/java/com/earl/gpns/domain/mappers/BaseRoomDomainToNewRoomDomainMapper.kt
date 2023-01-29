package com.earl.gpns.domain.mappers

import com.earl.gpns.domain.models.NewRoomDtoDomain
import com.earl.gpns.ui.mappers.RoomDomainToNewRoomDomainMapper
import javax.inject.Inject

class BaseRoomDomainToNewRoomDomainMapper @Inject constructor() :
    RoomDomainToNewRoomDomainMapper<NewRoomDtoDomain> {

    override fun map(
        roomId: String,
        image: String,
        title: String,
        lastMessage: String,
        lastMessageAuthor: String,
        deletable: Boolean,
        contactIsOnline: Int,
        contactLastAuth: String,
        lastMsgTimestamp: String
    ) = NewRoomDtoDomain.Base(roomId, title, image, lastMessageAuthor, lastMessageAuthor, lastMessage, lastMessageAuthor, contactIsOnline, contactLastAuth, lastMsgTimestamp)
}