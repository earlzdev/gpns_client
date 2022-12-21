package com.earl.gpns.data.models.remote.responses

import com.earl.gpns.data.mappers.GroupLastMessageResponseToDataMapper
import kotlinx.serialization.Serializable

@Serializable
data class GroupLastMessageResponse(
    val groupId: String,
    val authorName: String,
    val authorImage: String,
    val messageText: String,
    val timestamp: String,
    val read: Int
) {
    fun <T> map(mapper: GroupLastMessageResponseToDataMapper<T>) =
        mapper.map(groupId, authorName, authorImage, messageText, timestamp, read)
}
