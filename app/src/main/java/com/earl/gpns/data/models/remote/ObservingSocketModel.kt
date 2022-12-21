package com.earl.gpns.data.models.remote

import kotlinx.serialization.Serializable

@Serializable
data class ObservingSocketModel (
    val action: String,
    val value: String
)