package com.earl.gpns.data.models.remote

import com.earl.gpns.data.mappers.MessageRemoteToDataMapper
import kotlinx.serialization.Serializable

@Serializable
data class MessageRemote(
    val messageId: String,
    val roomId: String,
    val authorId: String,
    val timestamp: String,
    val messageText: String,
    val messageDate: String,
    val read: Int
) {
    fun <T> map(mapper: MessageRemoteToDataMapper<T>) = mapper.map(messageId, roomId, authorId, timestamp, messageText, messageDate, read)
}