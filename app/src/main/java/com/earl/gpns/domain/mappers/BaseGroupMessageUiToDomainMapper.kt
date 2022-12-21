package com.earl.gpns.domain.mappers

import com.earl.gpns.domain.models.GroupMessageDomain
import com.earl.gpns.ui.mappers.GroupMessageUiToDomainMapper
import javax.inject.Inject

class BaseGroupMessageUiToDomainMapper @Inject constructor() : GroupMessageUiToDomainMapper<GroupMessageDomain> {

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