package com.earl.gpns.data.models.remote.requests

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class UserNameDto(
    @SerializedName("name") val name: String
)
