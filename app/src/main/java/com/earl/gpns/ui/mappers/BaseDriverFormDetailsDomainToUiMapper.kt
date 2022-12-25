package com.earl.gpns.ui.mappers

import com.earl.gpns.domain.mappers.DriverFormDetailsDomainToUiMapper
import com.earl.gpns.ui.models.DriverFormDetailsUi
import javax.inject.Inject

class BaseDriverFormDetailsDomainToUiMapper @Inject constructor() : DriverFormDetailsDomainToUiMapper<DriverFormDetailsUi> {

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
    ) = DriverFormDetailsUi.Base(
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