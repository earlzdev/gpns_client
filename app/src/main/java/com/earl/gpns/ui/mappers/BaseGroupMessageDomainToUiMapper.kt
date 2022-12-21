package com.earl.gpns.ui.mappers

import com.earl.gpns.domain.mappers.GroupMessageDomainToUiMapper
import com.earl.gpns.ui.models.GroupMessageUi
import javax.inject.Inject

class BaseGroupMessageDomainToUiMapper @Inject constructor() : GroupMessageDomainToUiMapper<GroupMessageUi> {

    override fun map(
        groupId: String,
        messageId: String,
        authorName: String,
        authorImage: String,
        timestamp: String,
        messageText: String,
        read: Int
    ) = GroupMessageUi.Base(groupId, messageId, authorName, authorImage, timestamp, messageText, read)
}