package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.DriverFormDetailsData
import javax.inject.Inject

class BaseDriverFormDetailsRemoteToDataMapper @Inject constructor() : DriverTripFormDetailsRemoteToDataMapper<DriverFormDetailsData> {

    override fun map(
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
        passengersCount: Int,
        carGovNumber: String,
        tripPrice: Int,
        driverComment: String
    ) = DriverFormDetailsData.Base(
        driveFrom, driveTo, catchCompanionFrom, alsoCanDriveTo, schedule, ableToDriveInTurn, actualTripTime, car, carModel, carColor, passengersCount, carGovNumber, tripPrice, driverComment
    )
}