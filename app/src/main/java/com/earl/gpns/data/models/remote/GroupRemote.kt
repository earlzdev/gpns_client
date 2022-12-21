package com.earl.gpns.data.models.remote

import com.earl.gpns.data.mappers.GroupRemoteToDataMapper
import com.earl.gpns.data.models.GroupData
import kotlinx.serialization.Serializable

@Serializable
data class GroupRemote(
    val id: String,
    val title: String,
    val image: String,
    val lastMsgText: String?,
    val lastMsgAuthor: String?,
    val lastMsgTimestamp: String?,
    val lastMsgAuthorImage: String?,
    val companionGroup: Int?,
    val messagesCount: Int?,
    val lastMsgRead: Int
) {
    fun map(mapper: GroupRemoteToDataMapper<GroupData>) =
        mapper.map(id, title, image, lastMsgText, lastMsgAuthor, lastMsgAuthorImage, lastMsgTimestamp, companionGroup, messagesCount, lastMsgRead)
}