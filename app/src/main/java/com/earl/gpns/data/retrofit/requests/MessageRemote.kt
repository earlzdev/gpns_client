package com.earl.gpns.data.retrofit.requests

import com.earl.gpns.data.mappers.MessageRemoteToDataMapper
import kotlinx.serialization.Serializable

@Serializable
data class MessageRemote(
    val messageId: String,
    val roomId: String,
    val authorId: String,
    val timestamp: String,
    val messageText: String,
    private val messageDate: String
) {
    fun <T> map(mapper: MessageRemoteToDataMapper<T>) = mapper.map(messageId, roomId, authorId, timestamp, messageText, messageDate)
}