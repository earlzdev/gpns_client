package com.earl.gpns.data.models.remote.responses

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class TypingMessageDtoResponse(
    @SerializedName("roomId") val roomId: String,
    @SerializedName("username") val username: String,
    @SerializedName("typing") val typing: Int
)
