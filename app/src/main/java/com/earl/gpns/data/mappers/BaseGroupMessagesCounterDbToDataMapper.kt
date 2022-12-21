package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.GroupMessagesCounterData
import javax.inject.Inject

class BaseGroupMessagesCounterDbToDataMapper @Inject constructor() : GroupMessagesCounterDbToDataMapper<GroupMessagesCounterData> {

    override fun map(groupId: String, counter: Int) =
        GroupMessagesCounterData.Base(groupId, counter)
}