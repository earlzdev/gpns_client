package com.earl.gpns.domain.mappers

import com.earl.gpns.data.mappers.GroupMessagesCounterDataToDomainMapper
import com.earl.gpns.domain.models.GroupMessagesCounterDomain
import javax.inject.Inject

class BaseGroupMessagesCounterDataToDomainMapper @Inject constructor(): GroupMessagesCounterDataToDomainMapper<GroupMessagesCounterDomain> {

    override fun map(groupId: String, counter: Int) =
        GroupMessagesCounterDomain.Base(groupId, counter)
}