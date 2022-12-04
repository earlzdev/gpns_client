package com.earl.gpns.data.models.remote.responses

import com.earl.gpns.data.mappers.NewLastMsgResponseToDataMapper
import kotlinx.serialization.Serializable

@Serializable
data class NewLastMessageInRoomResponse(
    val roomId: String,
    val authorName: String,
    val authorImage: String,
    val timestamp: String,
    val messageText: String
) {
    fun <T> map(mapper: NewLastMsgResponseToDataMapper<T>) = mapper.map(roomId, authorName, authorImage, timestamp, messageText)
}
