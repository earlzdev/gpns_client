package com.earl.gpns.ui.models

data class FirstPartOfNewDriverForm(
    val driveFrom: String,
    val driveTo: String,
    val catchCompanionFrom: String,
    val alsoCanDriveTo: String,
    val schedule: String,
    val ableToDriveInTurn: Int,
    val actualTripTime: String
)
