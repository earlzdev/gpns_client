package com.earl.gpns.data.mappers

interface TripFormRemoteToDataMapper<T> {

    fun mapDriverDetails(
        username: String,
        userImage: String,
        companionRole: String,
        from: String,
        to: String,
        schedule: String,
        details: String,
        active: Int
    ) : T

    fun mapCompanionDetails(
        username: String,
        userImage: String,
        companionRole: String,
        from: String,
        to: String,
        schedule: String,
        details: String,
        active: Int
    ) : T
}