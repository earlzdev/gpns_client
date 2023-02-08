package com.earl.gpns.data.models.remote.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserAvatarRequest(
    val newImageString: String
)
