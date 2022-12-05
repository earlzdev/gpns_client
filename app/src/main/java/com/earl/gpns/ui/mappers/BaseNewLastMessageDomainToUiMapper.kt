package com.earl.gpns.ui.mappers

import com.earl.gpns.domain.mappers.NewLastMessageInRoomDomainToUiMapper
import com.earl.gpns.ui.models.NewLastMessageInRoomUi
import javax.inject.Inject

class BaseNewLastMessageDomainToUiMapper @Inject constructor(): NewLastMessageInRoomDomainToUiMapper<NewLastMessageInRoomUi> {

    override fun map(
        roomId: String,
        authorName: String,
        authorImage: String,
        timestamp: String,
        messageText: String,
        read: Int
    ) = NewLastMessageInRoomUi.Base(roomId, authorName, authorImage, timestamp, messageText, read)
}