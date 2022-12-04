package com.earl.gpns.data.models.remote.requests

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)
