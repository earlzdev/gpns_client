package com.earl.gpns.data.models

import kotlinx.serialization.Serializable

@Serializable
data class WebSocketValue<T>(
    val title: String,
    val value: T
)
