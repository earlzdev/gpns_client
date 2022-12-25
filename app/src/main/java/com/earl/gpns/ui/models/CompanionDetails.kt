package com.earl.gpns.ui.models

import com.earl.gpns.ui.SearchFormsDetails

data class CompanionDetails(
    val actualTripTime: String,
    val ableToPay: String,
    val comment: String
) : SearchFormsDetails
