package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.remote.requests.TypingStatusInGroupRequest
import javax.inject.Inject

class BaseGroupTypingStatusDataToRequestMapper @Inject constructor(): GroupTypingStatusDataToRequestMapper<TypingStatusInGroupRequest> {

    override fun map(groupId: String, username: String, status: Int) =
        TypingStatusInGroupRequest(groupId, username, status)
}