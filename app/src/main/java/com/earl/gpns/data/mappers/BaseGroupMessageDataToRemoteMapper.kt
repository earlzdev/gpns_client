package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.remote.GroupMessageRemote
import javax.inject.Inject

class BaseGroupMessageDataToRemoteMapper @Inject constructor() : GroupMessageDataToRemoteMapper<GroupMessageRemote> {

    override fun map(
        groupId: String,
        messageId: String,
        authorName: String,
        authorImage: String,
        timestamp: String,
        messageText: String,
        read: Int
    ) = GroupMessageRemote(groupId, messageId, authorName, authorImage, timestamp, messageText, read)
}