package com.earl.gpns.data.models.remote.requests

@kotlinx.serialization.Serializable
data class RemoveCompanionFromGroupDto(
    val username: String,
    val groupId: String
)
