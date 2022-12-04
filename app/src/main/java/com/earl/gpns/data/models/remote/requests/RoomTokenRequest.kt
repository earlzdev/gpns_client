package com.earl.gpns.data.models.remote.requests

import kotlinx.serialization.Serializable

@Serializable
data class RoomTokenRequest(
    val roomId: String
)
