package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.GroupTypingStatusData
import javax.inject.Inject

class BaseGroupTypingStatusRemoteToDataMapper @Inject constructor(): GroupTypingStatusRemoteToDataMapper<GroupTypingStatusData> {

    override fun map(groupId: String, username: String, status: Int) =
        GroupTypingStatusData.Base(groupId, username, status)
}