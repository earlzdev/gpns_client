package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.remote.MessageRemote
import javax.inject.Inject

class BaseMessageDataToRemoteMapper @Inject constructor() : MessageDataToRemoteMapper<MessageRemote> {

    override fun map(
        messageId: String,
        roomId: String,
        authorId: String,
        timestamp: String,
        messageText: String,
        messageDate: String,
        read: Int
    ) = MessageRemote(messageId, roomId, authorId, timestamp, messageText, messageDate, read)
}