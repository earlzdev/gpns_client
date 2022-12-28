package com.earl.gpns.ui.models

import com.earl.gpns.ui.mappers.CompanionFormUiToDomainMapper

interface CompanionFormUi {

    fun <T> mapToDomain(mapper: CompanionFormUiToDomainMapper<T>) : T

    fun provideCompanionDetailsUi() : CompanionDetailsUi

    class Base(
        private val username: String,
        private val userImage: String,
        private val from: String,
        private val to: String,
        private val schedule: String,
        private val actualTripTime: String,
        private val ableToPay: String?,
        private val comment: String
    ) : CompanionFormUi {
        override fun <T> mapToDomain(mapper: CompanionFormUiToDomainMapper<T>) =
            mapper.map(username, userImage, from, to, schedule, actualTripTime, ableToPay, comment)

        override fun provideCompanionDetailsUi() = CompanionDetailsUi(
            username,
            userImage,
            from,
            to,
            schedule,
            actualTripTime,
            ableToPay ?: "",
            comment
        )
    }
}