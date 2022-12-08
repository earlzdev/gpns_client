package com.earl.gpns.data.models.remote.requests

data class MarkAuthoredMessageAsReadRequest(
    val roomId: String,
    val authorName: String
)
