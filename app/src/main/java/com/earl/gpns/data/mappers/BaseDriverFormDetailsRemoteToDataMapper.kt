package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.DriverFormDetailsData
import javax.inject.Inject

class BaseDriverFormDetailsRemoteToDataMapper @Inject constructor() : DriverTripFormDetailsRemoteToDataMapper<DriverFormDetailsData> {

    override fun map(
        catchCompanionFrom: String,
        alsoCanDriveTo: String,
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
       catchCompanionFrom, alsoCanDriveTo, ableToDriveInTurn, actualTripTime, car, carModel, carColor, passengersCount, carGovNumber, tripPrice, driverComment
    )
}