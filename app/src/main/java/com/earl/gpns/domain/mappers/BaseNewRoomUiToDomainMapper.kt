package com.earl.gpns.domain.mappers

import com.earl.gpns.domain.models.NewRoomDtoDomain
import com.earl.gpns.ui.mappers.NewRoomUiToDomainMapper
import javax.inject.Inject

class BaseNewRoomUiToDomainMapper @Inject constructor() : NewRoomUiToDomainMapper<NewRoomDtoDomain> {

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
    ) = NewRoomDtoDomain.Base(
        roomId, name, image, author, contact, lastMessage, lastMessageAuthor, contactIsOnline, contactLastAuth, lastMsgTimestamp
    )
}