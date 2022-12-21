package com.earl.gpns.ui.mappers

import com.earl.gpns.domain.mappers.GroupLastMessageDomainToUiMapper
import com.earl.gpns.ui.models.GroupLastMessageUi
import javax.inject.Inject

class BaseGroupLastMessageDomainToUiMapper @Inject constructor() :GroupLastMessageDomainToUiMapper<GroupLastMessageUi> {

    override fun map(
        groupsId: String,
        authorName: String,
        authorImage: String,
        msgText: String,
        timestamp: String,
        read: Int
    ) = GroupLastMessageUi.Base(groupsId, authorName, authorImage, msgText, timestamp, read)
}