package com.earl.gpns.data.models.remote.responses

import com.earl.gpns.data.mappers.UserResponseToDataMapper
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse (
    @SerializedName("userId") val userId: String,
    @SerializedName("image") val image: String,
    @SerializedName("username") val username: String,
    @SerializedName("online") val online: Int,
    @SerializedName("lastAuth") val lastAuth: String,
    @SerializedName("tripRole") val tripRole: String,
) {
    fun <T> map(mapper: UserResponseToDataMapper<T>) = mapper.map(userId, image, username, online, lastAuth, tripRole)
}