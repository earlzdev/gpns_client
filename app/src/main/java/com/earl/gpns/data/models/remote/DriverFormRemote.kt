package com.earl.gpns.data.models.remote

import com.earl.gpns.data.mappers.DriverFormRemoteToDataMapper
import kotlinx.serialization.Serializable

@Serializable
data class DriverFormRemote(
    val username: String,
    val userImage: String,
    val driveFrom: String,
    val driveTo: String,
    val catchCompanionFrom: String,
    val alsoCanDriveTo: String,
    val schedule: String,
    val ableToDriveInTurn: Int,
    val actualTripTime: String,
    val car: String,
    val carModel: String,
    val carColor: String,
    val passengersCount: Int,
    val carGovNumber: String,
    val tripPrice: Int,
    val driverComment: String,
    val active: Int
) {
    fun <T> mapToData(mapper: DriverFormRemoteToDataMapper<T>) = mapper.map(
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
        passengersCount.toString(),
        carGovNumber,
        tripPrice.toString(),
        driverComment,
        active
    )
}
