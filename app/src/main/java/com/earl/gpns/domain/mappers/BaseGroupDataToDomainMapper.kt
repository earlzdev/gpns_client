package com.earl.gpns.domain.mappers

import com.earl.gpns.data.mappers.GroupDataToDomainMapper
import com.earl.gpns.domain.models.GroupDomain
import javax.inject.Inject

class BaseGroupDataToDomainMapper @Inject constructor() : GroupDataToDomainMapper<GroupDomain> {

    override fun map(
        groupId: String,
        title: String,
        image: String,
        lastMessage: String,
        lastMessageAuthor: String,
        lastMessageAuthorImage: String,
        lastMessageTimestamp: String,
        companionGroup: Int,
        messagesCount: Int,
        lastMsgRead: Int
    ) = GroupDomain.Base(groupId, title, image, lastMessage, lastMessageAuthor, lastMessageAuthorImage, lastMessageTimestamp, companionGroup, messagesCount, lastMsgRead)
}