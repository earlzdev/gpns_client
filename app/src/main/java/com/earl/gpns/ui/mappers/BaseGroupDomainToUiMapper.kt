package com.earl.gpns.ui.mappers

import com.earl.gpns.domain.mappers.GroupDomainToUiMapper
import com.earl.gpns.ui.models.GroupUi
import javax.inject.Inject

class BaseGroupDomainToUiMapper @Inject constructor(): GroupDomainToUiMapper<GroupUi> {

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
    ) = GroupUi.Base(groupId, title, image, lastMessage, lastMessageAuthor, lastMessageAuthorImage, lastMessageTimestamp, companionGroup, messagesCount, lastMsgRead)
}