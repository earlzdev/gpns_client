package com.earl.gpns.data.models.remote

import com.earl.gpns.data.mappers.DriverTripFormDetailsRemoteToDataMapper
import com.earl.gpns.data.models.DriverFormDetailsData
import com.earl.gpns.domain.TripFormDetails
import kotlinx.serialization.Serializable

@Serializable
data class DriverTripFormDetailsRemote(
    val driveFrom: String,
    val driveTo: String,
    val catchCompanionFrom: String,
    val alsoCanDriveTo: String,
    val schedule: String,
    val ableToDriveInTurn: Int,
    val actualTripTime: String,
    val car: String,
    val carModel: String,
    val carColor: String,
    val passengersCount: Int,
    val carGovNumber: String,
    val tripPrice: Int,
    val driverComment: String
) : TripFormDetails {
    fun map(mapper: DriverTripFormDetailsRemoteToDataMapper<DriverFormDetailsData>) =
        mapper.map(
            driveFrom,
            driveTo,
            catchCompanionFrom,
            alsoCanDriveTo,
            schedule,
            ableToDriveInTurn,
            actualTripTime,
            car,
            carModel,
            carColor,
            passengersCount,
            carGovNumber,
            tripPrice,
            driverComment
        )
}
