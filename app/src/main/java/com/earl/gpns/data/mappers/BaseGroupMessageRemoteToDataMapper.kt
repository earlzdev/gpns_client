package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.GroupMessageData
import javax.inject.Inject

class BaseGroupMessageRemoteToDataMapper @Inject constructor() : GroupMessageRemoteToDataMapper<GroupMessageData> {

    override fun map(
        groupId: String,
        messageId: String,
        authorName: String,
        authorImage: String,
        timestamp: String,
        messageText: String,
        read: Int
    ) = GroupMessageData.Base(groupId, messageId, authorName, authorImage, timestamp, messageText, read)
}