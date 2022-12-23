package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.remote.DriverFormRemote
import javax.inject.Inject

class BaseDriverFormDataToRemoteMapper @Inject constructor() : DriverFormDataToRemoteMapper<DriverFormRemote> {

    override fun map(
        username: String,
        userImage: String,
        driveFrom: String,
        driveTo: String,
        catchCompanionFrom: String,
        alsoCanDriveTo: String,
        schedule: String,
        ableToDriveInTurn: Int,
        actualTripTime: String,
        car: String,
        carModel: String,
        carColor: String,
        passengersCount: String,
        carGovNumber: String,
        tripPrice: String,
        driverComment: String
    ) = DriverFormRemote(
        username,
        userImage,
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
        passengersCount.toInt(),
        carGovNumber,
        tripPrice.toInt(),
        driverComment
    )
}