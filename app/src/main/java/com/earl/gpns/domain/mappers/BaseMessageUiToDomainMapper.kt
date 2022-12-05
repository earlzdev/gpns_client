package com.earl.gpns.domain.mappers

import com.earl.gpns.domain.models.MessageDomain
import com.earl.gpns.ui.mappers.MessageUiToDomainMapper
import javax.inject.Inject

class BaseMessageUiToDomainMapper @Inject constructor(): MessageUiToDomainMapper<MessageDomain> {

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