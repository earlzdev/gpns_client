package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.GroupLastMessageData
import javax.inject.Inject

class BaseGroupLastMessageResponseToDataMapper @Inject constructor() : GroupLastMessageResponseToDataMapper<GroupLastMessageData> {

    override fun map(
        groupsId: String,
        authorName: String,
        authorImage: String,
        msgText: String,
        timestamp: String,
        read: Int
    ) = GroupLastMessageData.Base(groupsId, authorName, authorImage, msgText, timestamp, read)
}