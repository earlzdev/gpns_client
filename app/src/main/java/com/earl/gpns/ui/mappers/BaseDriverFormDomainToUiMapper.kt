package com.earl.gpns.ui.mappers

import com.earl.gpns.domain.mappers.DriverFormDomainToUiMapper
import com.earl.gpns.ui.models.DriverFormUi
import javax.inject.Inject

class BaseDriverFormDomainToUiMapper @Inject constructor() : DriverFormDomainToUiMapper<DriverFormUi> {

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
    ) = DriverFormUi.Base(
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
        passengersCount,
        carGovNumber,
        tripPrice,
        driverComment,
        active
    )
}