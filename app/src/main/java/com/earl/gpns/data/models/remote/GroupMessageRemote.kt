package com.earl.gpns.data.models.remote

import com.earl.gpns.data.mappers.GroupMessageRemoteToDataMapper
import com.earl.gpns.data.models.GroupMessageData
import kotlinx.serialization.Serializable

@Serializable
data class GroupMessageRemote(
    val groupId: String,
    val messageId: String,
    val authorName: String,
    val authorImage: String,
    val timestamp: String,
    val messageText: String,
    val read: Int
) {
    fun map(mapper: GroupMessageRemoteToDataMapper<GroupMessageData>) =
        mapper.map(groupId, messageId, authorName, authorImage, timestamp, messageText, read)
}
