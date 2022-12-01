package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.MessageData
import com.earl.gpns.domain.mappers.MessageDomainToDataMapper
import javax.inject.Inject

class BaseMessageDomainToDataMapper @Inject constructor(): MessageDomainToDataMapper<MessageData> {
    override fun map(
        messageId: String,
        roomId: String,
        authorId: String,
        timestamp: String,
        messageText: String,
        messageDate: String
    ) = MessageData.Base(messageId, roomId, authorId, timestamp, messageText, messageDate)
}