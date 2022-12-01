package com.earl.gpns.data.retrofit.requests

import kotlinx.serialization.Serializable

@Serializable
data class RoomTokenRequest(
    val roomId: String
)
