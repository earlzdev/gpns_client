package com.earl.gpns.domain.mappers

import com.earl.gpns.data.mappers.GroupTypingStatusDataToDomainMapper
import com.earl.gpns.domain.models.GroupTypingStatusDomain
import javax.inject.Inject

class BaseGroupTypingStatusDataToDomainMapper @Inject constructor(): GroupTypingStatusDataToDomainMapper<GroupTypingStatusDomain> {

    override fun map(groupId: String, username: String, status: Int) =
        GroupTypingStatusDomain.Base(groupId, username, status)
}