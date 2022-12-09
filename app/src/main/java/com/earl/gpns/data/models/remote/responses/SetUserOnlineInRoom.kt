package com.earl.gpns.data.models.remote.responses

import kotlinx.serialization.Serializable

@Serializable
data class SetUserOnlineInRoom(
    val online: Int,
    val username: String,
    val roomId: String,
    val lastAuthDate: String
)