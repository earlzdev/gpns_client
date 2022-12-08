package com.earl.gpns.data.models.remote.responses

import com.earl.gpns.data.mappers.RoomResponseToDataMapper
import com.earl.gpns.data.models.RoomData
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RoomResponse(
    @SerializedName("roomId") val roomId: String,
    @SerializedName("image") val image: String,
    @SerializedName("title") val title: String,
    @SerializedName("lastMessage") val lastMessage: String,
    @SerializedName("lastMessageAuthor") val lastMessageAuthor: String,
    @SerializedName("deletable") val deletable: Boolean,
    @SerializedName("unreadMsgCounter") val unreadMsgCounter: Int,
    @SerializedName("lastMsgRead") val lastMsgRead: Int,
) {
    fun map(mapper: RoomResponseToDataMapper<RoomData>) =
        mapper.map(roomId, image, title, lastMessage, lastMessageAuthor, deletable, unreadMsgCounter, lastMsgRead)
}
