package com.earl.gpns.data.models.remote.responses

import kotlinx.serialization.Serializable

@Serializable
data class MarkMessagesAsReadInGroupResponse (
    val groupId: String
)