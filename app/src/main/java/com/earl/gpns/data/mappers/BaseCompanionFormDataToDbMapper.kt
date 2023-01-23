package com.earl.gpns.data.mappers

import com.earl.gpns.data.localDb.CompanionFormDb
import javax.inject.Inject

class BaseCompanionFormDataToDbMapper @Inject constructor() : CompanionFormDataToDbMapper<CompanionFormDb> {

    override fun map(
        username: String,
        userImage: String,
        from: String,
        to: String,
        schedule: String,
        actualTripTime: String,
        ableToPay: String?,
        comment: String
    ) = CompanionFormDb(
        0, username, userImage, from, to, schedule, actualTripTime, ableToPay, comment
    )
}