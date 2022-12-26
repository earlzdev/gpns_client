package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.CompanionFormDetailsData
import com.earl.gpns.data.models.DriverFormDetailsData
import com.earl.gpns.data.models.TripFormData
import com.earl.gpns.data.models.remote.CompanionFormDetailsRemote
import com.earl.gpns.data.models.remote.DriverFormDetailsRemote
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class BaseTripFormRemoteToDataMapper @Inject constructor(
    private val companionFormDetailsRemoteToDataMapper: CompanionTripFormDetailsRemoteToDataMapper<CompanionFormDetailsData>,
    private val driverFormDetailsRemoteToDataMapper: DriverTripFormDetailsRemoteToDataMapper<DriverFormDetailsData>,
) : TripFormRemoteToDataMapper<TripFormData> {

    override fun mapDriverDetails(
        username: String,
        userImage: String,
        companionRole: String,
        from: String,
        to: String,
        schedule: String,
        details: String
    ) = TripFormData.Base(
        username,
        userImage,
        companionRole,
        from,
        to,
        schedule,
        Json.decodeFromString<DriverFormDetailsRemote>(details)
            .map(driverFormDetailsRemoteToDataMapper)
    )

    override fun mapCompanionDetails(
        username: String,
        userImage: String,
        companionRole: String,
        from: String,
        to: String,
        schedule: String,
        details: String
    ) = TripFormData.Base(
        username,
        userImage,
        companionRole,
        from,
        to,
        schedule,
        Json.decodeFromString<CompanionFormDetailsRemote>(details)
            .map(companionFormDetailsRemoteToDataMapper)
    )
}