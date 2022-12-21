package com.earl.gpns.data.models.remote.requests

import kotlinx.serialization.Serializable

@Serializable
data class TypingStatusInGroupRequest(
    val groupId: String,
    val username: String,
    val status: Int
)
