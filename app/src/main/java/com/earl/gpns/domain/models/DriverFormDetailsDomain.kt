package com.earl.gpns.domain.models

import com.earl.gpns.domain.TripFormDetails
import com.earl.gpns.domain.mappers.DriverFormDetailsDomainToUiMapper

interface DriverFormDetailsDomain : TripFormDetails {

    fun <T> map(mapper: DriverFormDetailsDomainToUiMapper<T>) : T

    class Base(
        private val catchCompanionFrom: String,
        private val alsoCanDriveTo: String,
        private val ableToDriveInTurn: Int,
        private val actualTripTime: String,
        private val car: String,
        private val carModel: String,
        private val carColor: String,
        private val passengersCount: Int,
        private val carGovNumber: String,
        private val tripPrice: Int,
        private val driverComment: String
    ) : DriverFormDetailsDomain {
        override fun <T> map(mapper: DriverFormDetailsDomainToUiMapper<T>) =
            mapper.map(
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
}