package com.earl.gpns.ui.models

import com.earl.gpns.domain.TripFormDetails

interface DriverFormDetailsUi : TripFormDetails {

    class Base(
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
        private val passengersCount: Int,
        private val carGovNumber: String,
        private val tripPrice: Int,
        private val driverComment: String
    ) : DriverFormDetailsUi {

    }
}