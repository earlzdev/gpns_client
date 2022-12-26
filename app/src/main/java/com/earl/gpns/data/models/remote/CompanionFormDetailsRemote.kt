package com.earl.gpns.data.models.remote

import com.earl.gpns.data.mappers.CompanionTripFormDetailsRemoteToDataMapper
import com.earl.gpns.data.models.CompanionFormDetailsData

@kotlinx.serialization.Serializable
data class CompanionFormDetailsRemote(
    private val actualTripTime: String,
    private val ableToPay: String,
    private val comment: String
) {
    fun map(mappper: CompanionTripFormDetailsRemoteToDataMapper<CompanionFormDetailsData>) =
        mappper.map(actualTripTime, ableToPay, comment)
}
