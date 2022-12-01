package com.earl.gpns.data.retrofit.requests

import kotlinx.serialization.Serializable

@Serializable
data class RoomMemberRequest(
    val username: String,
    val roomId: String
)
