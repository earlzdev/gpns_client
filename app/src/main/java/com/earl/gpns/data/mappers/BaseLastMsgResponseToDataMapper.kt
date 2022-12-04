package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.NewLastMessageInRoomData
import javax.inject.Inject

class BaseLastMsgResponseToDataMapper @Inject constructor(): NewLastMsgResponseToDataMapper<NewLastMessageInRoomData> {

    override fun map(
        roomId: String,
        authorName: String,
        authorImage: String,
        timestamp: String,
        messageText: String
    ) = NewLastMessageInRoomData.Base(roomId, authorName, authorImage, timestamp, messageText)
}