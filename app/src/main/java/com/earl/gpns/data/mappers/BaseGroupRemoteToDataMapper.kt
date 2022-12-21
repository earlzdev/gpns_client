package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.GroupData
import javax.inject.Inject

class BaseGroupRemoteToDataMapper @Inject constructor(): GroupRemoteToDataMapper<GroupData> {

    override fun map(
        groupId: String,
        title: String,
        image: String,
        lastMessage: String?,
        lastMessageAuthor: String?,
        lastMessageAuthorImage: String?,
        lastMessageTimestamp: String?,
        companionGroup: Int?,
        messagesCount: Int?,
        lastMsgRead: Int?
    ) = GroupData.Base(
        groupId,
        title,
        image,
        lastMessage ?: "",
        lastMessageAuthor ?: "",
        lastMessageAuthorImage ?: "",
        lastMessageTimestamp ?: "",
        companionGroup ?: 0,
        messagesCount ?: 0,
        lastMsgRead ?: 0
    )
}