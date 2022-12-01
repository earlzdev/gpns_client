package com.earl.gpns.ui.mappers

import com.earl.gpns.domain.mappers.MessageDomainToUiMapper
import com.earl.gpns.ui.models.MessageUi
import javax.inject.Inject

class BaseMessageDomainToUiMapper @Inject constructor() : MessageDomainToUiMapper<MessageUi> {

    override fun map(
        messageId: String,
        roomId: String,
        authorId: String,
        timestamp: String,
        messageText: String,
        messageDate: String
    ) = MessageUi.Base(messageId, roomId, authorId, timestamp, messageText, messageDate)
}