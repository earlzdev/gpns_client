package com.earl.gpns.domain.mappers

import com.earl.gpns.domain.models.GroupTypingStatusDomain
import com.earl.gpns.ui.mappers.GroupTypingStatusUiToDomainMapper
import javax.inject.Inject

class BaseGroupTypingStatusUiToDomainMapper @Inject constructor() : GroupTypingStatusUiToDomainMapper<GroupTypingStatusDomain> {

    override fun map(groupId: String, username: String, status: Int) =
        GroupTypingStatusDomain.Base(groupId, username, status)
}