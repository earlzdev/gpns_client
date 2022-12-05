package com.earl.gpns.domain.mappers

import com.earl.gpns.data.mappers.MessageDataToDomainMapper
import com.earl.gpns.domain.models.MessageDomain
import javax.inject.Inject

class BaseMessageDataToDomainMapper @Inject constructor() : MessageDataToDomainMapper<MessageDomain> {

    override fun map(
        messageId: String,
        roomId: String,
        authorId: String,
        timestamp: String,
        messageText: String,
        messageDate: String,
        read: Int
    ) = MessageDomain.Base(messageId, roomId, authorId, timestamp, messageText, messageDate, read)
}