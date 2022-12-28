package com.earl.gpns.ui.models

import com.earl.gpns.ui.SearchFormsDetails

data class DriverDetailsUi(
    val username: String,
    val userImage: String,
    val from: String,
    val to: String,
    val schedule: String,
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
