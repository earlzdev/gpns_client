package com.earl.gpns.data.models.remote

import com.earl.gpns.data.mappers.TripFormRemoteToDataMapper
import com.earl.gpns.data.models.TripFormData
import kotlinx.serialization.Serializable

@Serializable
data class TripFormRemote (
    val username: String,
    val userImage: String,
    val companionRole: String,
    val from: String,
    val to: String,
    val schedule: String,
    val details: String,
    val active: Int
) {
    fun map(mapper: TripFormRemoteToDataMapper<TripFormData>) : TripFormData {
        return if (companionRole == COMPANION_ROLE) {
            mapper.mapCompanionDetails(username, userImage, companionRole, from, to, schedule, details, active)
        } else {
            mapper.mapDriverDetails(username, userImage, companionRole, from, to, schedule, details, active)
        }
    }

    companion object {
        private const val COMPANION_ROLE = "COMPANION_ROLE"
    }
}
