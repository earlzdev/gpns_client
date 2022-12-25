package com.earl.gpns.ui.models

import com.earl.gpns.ui.SearchFormsDetails

data class DriverDetails(
    val catchCompanionFrom: String,
    val alsoCanDriveTo: String,
    val ableToDriveInTurn: Int,
    val actualTripTime: String,
    val car: String,
    val carModel: String,
    val carColor: String,
    val passengersCount: Int,
    val carGovNumber: String,
    val tripPrice: Int,
    val driverComment: String
): SearchFormsDetails
