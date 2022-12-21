package com.earl.gpns.ui.mappers

import com.earl.gpns.domain.mappers.GroupMessagesCounterDomainToUimapper
import com.earl.gpns.ui.models.GroupMessagesCounterUi
import javax.inject.Inject

class BaseGroupMessagesCounterDomainToUiMapper @Inject constructor(): GroupMessagesCounterDomainToUimapper<GroupMessagesCounterUi> {

    override fun map(groupId: String, counter: Int) =
        GroupMessagesCounterUi.Base(groupId, counter)
}