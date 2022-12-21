package com.earl.gpns.ui.mappers

import com.earl.gpns.domain.mappers.GroupTypingStatusDomainToUiMapper
import com.earl.gpns.ui.models.GroupTypingStatusUi
import javax.inject.Inject

class BaseGroupTypingStatusDomainToUiMapper @Inject constructor(): GroupTypingStatusDomainToUiMapper<GroupTypingStatusUi> {

    override fun map(groupId: String, username: String, status: Int) =
        GroupTypingStatusUi.Base(groupId, username, status)
}