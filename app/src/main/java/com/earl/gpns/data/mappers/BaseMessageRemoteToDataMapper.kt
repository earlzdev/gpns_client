package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.MessageData
import javax.inject.Inject

class BaseMessageRemoteToDataMapper @Inject constructor(): MessageRemoteToDataMapper<MessageData> {

    override fun map(
        messageId: String,
        roomId: String,
        authorId: String,
        timestamp: String,
        messageText: String,
        messageDate: String,
        read: Int
    ) = MessageData.Base(messageId, roomId, authorId, timestamp, messageText, messageDate, read)
}