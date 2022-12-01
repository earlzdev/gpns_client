package com.earl.gpns.ui.models

import android.widget.TextView
import com.earl.gpns.core.Same
import com.makeramen.roundedimageview.RoundedImageView

interface UserUi : Same<UserUi> {

    override fun same(value: UserUi) = this == value

    fun recyclerDetails(
        imageView: RoundedImageView,
        name: TextView,
        lastSeen: TextView
    )

    fun chatInfo() : ChatInfo

    fun provideName() : String

    fun provideImage() : String

    fun provideId() : String

    class Base(
        private val userId: String, // todo -> remove id
        private val image: String,
        private val username: String,
        private val online: String,
    ) : UserUi {

        override fun recyclerDetails(
            imageView: RoundedImageView,
            name: TextView,
            lastSeen: TextView
        ) {
            name.text = username
        }

        override fun chatInfo() = ChatInfo(null, username, image)

        override fun provideId() = userId

        override fun provideName() = username

        override fun provideImage() = image
    }
}