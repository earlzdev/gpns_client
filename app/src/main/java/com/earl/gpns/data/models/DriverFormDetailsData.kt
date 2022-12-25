package com.earl.gpns.data.models

import com.earl.gpns.data.mappers.DriverFormDetailsDataToDomainMapper
import com.earl.gpns.domain.TripFormDetails

interface DriverFormDetailsData : TripFormDetails {

    fun <T> map(mapper: DriverFormDetailsDataToDomainMapper<T>) : T

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
    ) : DriverFormDetailsData {
        override fun <T> map(mapper: DriverFormDetailsDataToDomainMapper<T>) =
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