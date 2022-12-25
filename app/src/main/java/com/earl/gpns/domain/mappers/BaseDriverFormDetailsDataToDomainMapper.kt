package com.earl.gpns.domain.mappers

import com.earl.gpns.data.mappers.DriverFormDetailsDataToDomainMapper
import com.earl.gpns.domain.models.DriverFormDetailsDomain
import javax.inject.Inject

class BaseDriverFormDetailsDataToDomainMapper @Inject constructor(): DriverFormDetailsDataToDomainMapper<DriverFormDetailsDomain> {

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
    ) = DriverFormDetailsDomain.Base(
        catchCompanionFrom,
        alsoCanDriveTo,
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