package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.DriverFormData
import com.earl.gpns.domain.mappers.DriverFormDomainToDataMapper
import javax.inject.Inject

class BaseDriverFormDomainToDataMapper @Inject constructor() : DriverFormDomainToDataMapper<DriverFormData> {

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
    ) = DriverFormData.Base(
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
        driverComment
    )
}