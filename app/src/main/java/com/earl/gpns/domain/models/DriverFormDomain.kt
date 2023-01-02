package com.earl.gpns.domain.models

import com.earl.gpns.domain.mappers.DriverFormDomainToDataMapper
import com.earl.gpns.domain.mappers.DriverFormDomainToUiMapper

interface DriverFormDomain {

    fun <T> mapToData(mapper: DriverFormDomainToDataMapper<T>) : T

    fun <T> mapToUi(mapper: DriverFormDomainToUiMapper<T>) : T

    class Base(
        private val username: String,
        private val userImage: String,
        private val driveFrom: String,
        private val driveTo: String,
        private val catchCompanionFrom: String,
        private val alsoCanDriveTo: String,
        private val schedule: String,
        private val ableToDriveInTurn: Int,
        private val actualTripTime: String,
        private val car: String,
        private val carModel: String,
        private val carColor: String,
        private val passengersCount: String,
        private val carGovNumber: String,
        private val tripPrice: String,
        private val driverComment: String,
        private val active: Int
    ) : DriverFormDomain {
        override fun <T> mapToData(mapper: DriverFormDomainToDataMapper<T>) =
            mapper.map(username, userImage, driveFrom, driveTo, catchCompanionFrom, alsoCanDriveTo, schedule, ableToDriveInTurn, actualTripTime, car, carModel, carColor, passengersCount, carGovNumber, tripPrice, driverComment, active)

        override fun <T> mapToUi(mapper: DriverFormDomainToUiMapper<T>) =
            mapper.map(username, userImage, driveFrom, driveTo, catchCompanionFrom, alsoCanDriveTo, schedule, ableToDriveInTurn, actualTripTime, car, carModel, carColor, passengersCount, carGovNumber, tripPrice, driverComment, active)
    }
}