package com.earl.gpns.ui.models

import android.widget.ImageView
import android.widget.TextView
import com.earl.gpns.core.Same
import com.earl.gpns.domain.TripFormDetails
import com.makeramen.roundedimageview.RoundedImageView

interface TripFormUi : Same<TripFormUi> {

    override fun same(value: TripFormUi) = this == value

    fun recyclerDetails(
        name: TextView,
        image: RoundedImageView,
        driverRole: ImageView,
        compRole: ImageView,
        from: TextView,
        to: TextView,
        schedule: TextView
    )

    class Base(
        private val username: String,
        private val userImage: String,
        private val companionRole: String,
        private val details: TripFormDetails
    ) : TripFormUi {
        override fun recyclerDetails(
            name: TextView,
            image: RoundedImageView,
            driverRole: ImageView,
            compRole: ImageView,
            from: TextView,
            to: TextView,
            schedule: TextView
        ) {

        }
    }
}