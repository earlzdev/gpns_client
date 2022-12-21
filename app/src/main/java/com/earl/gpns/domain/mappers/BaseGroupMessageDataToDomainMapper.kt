package com.earl.gpns.domain.mappers

import com.earl.gpns.data.mappers.GroupMessageDataToDomainMapper
import com.earl.gpns.domain.models.GroupMessageDomain
import javax.inject.Inject

class BaseGroupMessageDataToDomainMapper @Inject constructor(): GroupMessageDataToDomainMapper<GroupMessageDomain> {

    override fun map(
        groupId: String,
        messageId: String,
        authorName: String,
        authorImage: String,
        timestamp: String,
        messageText: String,
        read: Int
    ) = GroupMessageDomain.Base(groupId, messageId, authorName, authorImage, timestamp, messageText, read)
}