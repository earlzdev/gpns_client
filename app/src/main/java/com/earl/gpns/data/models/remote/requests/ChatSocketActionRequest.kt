package com.earl.gpns.data.models.remote.requests

import kotlinx.serialization.Serializable

@Serializable
data class ChatSocketActionRequest (
    val action: String,
    val userId: String,
    val value: String
)
