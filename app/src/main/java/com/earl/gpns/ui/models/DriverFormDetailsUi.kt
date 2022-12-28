package com.earl.gpns.ui.models

import com.earl.gpns.domain.TripFormDetails

interface DriverFormDetailsUi : TripFormDetails {

    fun provideDriverDetails(
        username: String,
        userImage: String,
        from: String,
        to: String,
        schedule: String
    ) : DriverDetailsUi

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
    ) : DriverFormDetailsUi {
        override fun provideDriverDetails(
            username: String,
            userImage: String,
            from: String,
            to: String,
            schedule: String
        ) = DriverDetailsUi(
            username,
            userImage,
            from,
            to,
            schedule,
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