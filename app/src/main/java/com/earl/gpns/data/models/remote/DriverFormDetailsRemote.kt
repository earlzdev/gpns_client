package com.earl.gpns.data.models.remote

import com.earl.gpns.data.mappers.DriverTripFormDetailsRemoteToDataMapper
import com.earl.gpns.data.models.DriverFormDetailsData

@kotlinx.serialization.Serializable
data class DriverFormDetailsRemote(
    private val catchCompanionFrom: String,
    private val alsoCanDriveTo: String,
    private val ableToDriveInTurn: Int,
    private val actualTripTime: String,
    private val car: String,
    private val carModel: String,
    private val carColor: String,
    private val passengersCount: Int,
    private val carGovNumber: String,
    private val tripPrice: Int,
    private val driverComment: String
) {
    fun map(mapper: DriverTripFormDetailsRemoteToDataMapper<DriverFormDetailsData>) =
        mapper.map(catchCompanionFrom, alsoCanDriveTo, ableToDriveInTurn, actualTripTime, car, carModel, carColor, passengersCount, carGovNumber, tripPrice, driverComment)
}
