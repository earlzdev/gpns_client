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
        driverComment: String,
        active: Int
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
        if (passengersCount == "") 0 else passengersCount.toInt(),
        carGovNumber,
        if (tripPrice == "По договоренности") 0 else tripPrice.toInt(),
        driverComment,
        active
    )
}