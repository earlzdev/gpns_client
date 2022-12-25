package com.earl.gpns.data.models.remote

import kotlinx.serialization.Serializable

@Serializable
data class TripFormRemote (
    val username: String,
    val userImage: String,
    val companionRole: String,
    val from: String,
    val to: String,
    val schedule: String,
    val details: String
)
