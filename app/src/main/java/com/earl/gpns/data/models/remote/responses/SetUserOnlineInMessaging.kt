package com.earl.gpns.data.models.remote.responses

import kotlinx.serialization.Serializable

@Serializable
data class SetUserOnlineInMessaging(
    val online: Int,
    val lastAuth: String
)