package com.earl.gpns.data.models.remote

import com.earl.gpns.data.mappers.CompanionTripFormDetailsRemoteToDataMapper
import com.earl.gpns.data.models.CompanionFormDetailsData
import com.earl.gpns.domain.TripFormDetails
import kotlinx.serialization.Serializable

@Serializable
data class CompanionTripFormDetailsRemote(
    val from: String,
    val to: String,
    val schedule: String,
    val actualTripTime: String,
    val ableToPay: String,
    val comment: String
) : TripFormDetails {
    fun map(mapper: CompanionTripFormDetailsRemoteToDataMapper<CompanionFormDetailsData>) =
        mapper.map(from, to, schedule, actualTripTime, ableToPay, comment)
}
