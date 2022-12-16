package com.earl.gpns.data.models.remote

import kotlinx.serialization.Serializable

@Serializable
data class RoomObservingSocketModel (
    val action: String,
    val value: String
)