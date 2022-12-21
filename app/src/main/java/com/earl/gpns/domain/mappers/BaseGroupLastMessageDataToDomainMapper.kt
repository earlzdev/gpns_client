package com.earl.gpns.domain.mappers

import com.earl.gpns.data.mappers.GroupLastMessageDataToDomainMapper
import com.earl.gpns.domain.models.GroupLastMessageDomain
import javax.inject.Inject

class BaseGroupLastMessageDataToDomainMapper @Inject constructor() : GroupLastMessageDataToDomainMapper<GroupLastMessageDomain> {

    override fun map(
        groupsId: String,
        authorName: String,
        authorImage: String,
        msgText: String,
        timestamp: String,
        read: Int
    ) = GroupLastMessageDomain.Base(groupsId, authorName, authorImage, msgText, timestamp, read)
}