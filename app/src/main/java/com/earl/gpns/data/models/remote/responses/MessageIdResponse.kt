package com.earl.gpns.data.models.remote.responses

import kotlinx.serialization.Serializable

@Serializable
data class MessageIdResponse(
    val messageId: String
)
