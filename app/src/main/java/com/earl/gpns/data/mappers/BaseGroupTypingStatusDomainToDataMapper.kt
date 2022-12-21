package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.GroupTypingStatusData
import com.earl.gpns.domain.mappers.GroupTypingStatusDomainToDataMapper
import javax.inject.Inject

class BaseGroupTypingStatusDomainToDataMapper @Inject constructor(): GroupTypingStatusDomainToDataMapper<GroupTypingStatusData> {

    override fun map(groupId: String, username: String, status: Int) =
        GroupTypingStatusData.Base(groupId, username, status)
}