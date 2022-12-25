package com.earl.gpns.ui.models

import com.earl.gpns.domain.TripFormDetails

interface CompanionFormDetailsUi : TripFormDetails {

    class Base(
        private val from: String,
        private val to: String,
        private val schedule: String,
        private val actualTripTime: String,
        private val ableToPay: String,
        private val comment: String
    ) : CompanionFormDetailsUi {

    }
}