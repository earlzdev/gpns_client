package com.earl.gpns.data.models.remote.requests

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class NewRoomRequest(
    @SerializedName("roomId") val roomId: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("author") val author: String,
    @SerializedName("contact") val contact: String,
    @SerializedName("lastMessage") val lastMessage: String,
    @SerializedName("lastMessageAuthor") val lastMessageAuthor: String,
    @SerializedName("contactIsOnline") val contactIsOnline: Int,
    @SerializedName("contactLastAuth") val contactLastAuth: String,
    @SerializedName("lastMsgTimestamp") val lastMsgTimestamp: String
)

