package com.earl.gpns.domain.mappers

import com.earl.gpns.data.mappers.DriverFormDataToDomainMapper
import com.earl.gpns.domain.models.DriverFormDomain
import javax.inject.Inject

class BaseDriverFormDataToDomainMapper @Inject constructor() : DriverFormDataToDomainMapper<DriverFormDomain> {

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
    ) = DriverFormDomain.Base(
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