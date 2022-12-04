package com.earl.gpns.domain.mappers

import com.earl.gpns.data.mappers.NewLastMsgDataToDomainMapper
import com.earl.gpns.domain.models.NewLastMessageInRoomDomain
import javax.inject.Inject

class BaseLastMsgDataToDomainMapper @Inject constructor(): NewLastMsgDataToDomainMapper<NewLastMessageInRoomDomain> {

    override fun map(
        roomId: String,
        authorName: String,
        authorImage: String,
        timestamp: String,
        messageText: String
    ) = NewLastMessageInRoomDomain.Base(roomId, authorName, authorImage, timestamp, messageText)
}