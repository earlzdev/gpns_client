package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.GroupMessageData
import com.earl.gpns.domain.mappers.GroupMessageDomainToDataMapper
import javax.inject.Inject

class BaseGroupMessageDomainToDataMapper @Inject constructor(): GroupMessageDomainToDataMapper<GroupMessageData> {

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