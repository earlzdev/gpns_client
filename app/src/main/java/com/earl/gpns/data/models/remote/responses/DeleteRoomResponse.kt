package com.earl.gpns.data.models.remote.responses

import kotlinx.serialization.Serializable

@Serializable
data class DeleteRoomResponse(
    val roomId: String
)
