package com.earl.gpns.data.mappers

interface DriverTripFormDetailsRemoteToDataMapper<T> {

    fun map(
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
    ) : T
}