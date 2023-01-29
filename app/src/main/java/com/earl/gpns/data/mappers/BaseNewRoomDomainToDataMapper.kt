package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.NewRoomDtoData
import com.earl.gpns.domain.mappers.NewRoomDomainToDataMapper
import javax.inject.Inject

class BaseNewRoomDomainToDataMapper @Inject constructor(): NewRoomDomainToDataMapper<NewRoomDtoData> {

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
    ) = NewRoomDtoData.Base(roomId, name, image, author, contact, lastMessage, lastMessageAuthor, contactIsOnline, contactLastAuth, lastMsgTimestamp)
}