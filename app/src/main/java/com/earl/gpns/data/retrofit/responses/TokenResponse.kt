package com.earl.gpns.data.retrofit.responses

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String
)
