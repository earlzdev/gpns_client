package com.earl.gpns.data.localDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.earl.gpns.data.mappers.DriverFormDbToDataMapper
import com.earl.gpns.data.models.DriverFormData

@Entity(tableName = "driverFormDb")
data class DriverFormDb(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "userImage") val userImage: String,
    @ColumnInfo(name = "driveFrom") val driveFrom: String,
    @ColumnInfo(name = "driveTo") val driveTo: String,
    @ColumnInfo(name = "catchCompanionFrom") val catchCompanionFrom: String,
    @ColumnInfo(name = "alsoCanDriveTo") val alsoCanDriveTo: String,
    @ColumnInfo(name = "schedule") val schedule: String,
    @ColumnInfo(name = "ableToDriveInTurn") val ableToDriveInTurn: Int,
    @ColumnInfo(name = "actualTripTime") val actualTripTime: String,
    @ColumnInfo(name = "car") val car: String,
    @ColumnInfo(name = "carModel") val carModel: String,
    @ColumnInfo(name = "carColor") val carColor: String,
    @ColumnInfo(name = "passengersCount") val passengersCount: String,
    @ColumnInfo(name = "carGovNumber") val carGovNumber: String,
    @ColumnInfo(name = "tripPrice") val tripPrice: String,
    @ColumnInfo(name = "driverComment") val driverComment: String,
) {
    fun mapToData(mapper: DriverFormDbToDataMapper<DriverFormData>) =
        mapper.map(username, userImage, driveFrom, driveTo, catchCompanionFrom, alsoCanDriveTo, schedule, ableToDriveInTurn, actualTripTime, car, carModel, carColor, passengersCount, carGovNumber, tripPrice, driverComment)
}
