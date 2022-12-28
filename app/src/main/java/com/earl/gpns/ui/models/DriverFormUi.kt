package com.earl.gpns.ui.models

import com.earl.gpns.ui.mappers.DriverFormUiToDomainMapper
import kotlinx.serialization.Serializable

interface DriverFormUi {

    fun <T> mapToDomain(mapper: DriverFormUiToDomainMapper<T>) : T

    fun provideDriverFormDetailsUi() : DriverDetailsUi

    @Serializable
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
        private val driverComment: String
    ) : DriverFormUi {
        override fun <T> mapToDomain(mapper: DriverFormUiToDomainMapper<T>) =
            mapper.map(
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

        override fun provideDriverFormDetailsUi() = DriverDetailsUi(
            username,
            userImage,
            driveFrom,
            driveTo,
            schedule,
            catchCompanionFrom,
            alsoCanDriveTo,
            ableToDriveInTurn,
            actualTripTime,
            car,
            carModel,
            carColor,
            passengersCount.toInt(),
            carGovNumber,
            tripPrice.toInt(),
            driverComment
        )
    }
}