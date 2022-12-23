package com.earl.gpns.data.models.remote

import com.earl.gpns.data.mappers.CompanionFormRemoteToDataMapper
import com.earl.gpns.data.models.CompanionFormData
import kotlinx.serialization.Serializable

@Serializable
data class CompanionFormRemote(
    val username: String,
    val userImage: String,
    val from: String,
    val to: String,
    val schedule: String,
    val actualTripTime: String,
    val ableToPay: String?,
    val comment: String
) {
    fun mapToData(mapper: CompanionFormRemoteToDataMapper<CompanionFormData>) =
        mapper.map(username, userImage, from, to, schedule, actualTripTime, ableToPay, comment)
}
