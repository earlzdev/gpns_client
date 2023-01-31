package com.earl.gpns.ui.models

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.earl.gpns.R
import com.earl.gpns.ui.core.Same
import com.makeramen.roundedimageview.RoundedImageView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

interface UserUi : Same<UserUi> {

    override fun same(value: UserUi) = this == value

    fun recyclerDetails(
        imageView: RoundedImageView,
        name: TextView,
        lastSeen: TextView,
        onlineIndicator: ImageView
    )

    fun companionGroupRecyclerDetails(
        imageView: RoundedImageView,
        name: TextView,
        role: TextView
    )

    fun chatInfo() : ChatInfo

    fun provideName() : String

    fun provideImage() : String

    fun provideId() : String

    class Base(
        private val userId: String,
        private val image: String,
        private val username: String,
        private val online: Int,
        private val lastAuth: String,
        private val tripRole: String
    ) : UserUi {

        override fun recyclerDetails(
            imageView: RoundedImageView,
            name: TextView,
            lastSeen: TextView,
            onlineIndicator: ImageView
        ) {
            val context = imageView.context
            name.text = username
            if (online == 0) {
                val currentDate = Date()
                val simpleDateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
                val standardFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                val dateText = simpleDateFormat.format(currentDate)
                val lastAuthDate = LocalDateTime.parse(lastAuth, standardFormatter)
                val dayOfYearFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
                val dayOfMonthFormatter = DateTimeFormatter.ofPattern("d MMMM")
                val timeOfDayFormatter = DateTimeFormatter.ofPattern("HH:mm")
                val currentDateText = LocalDateTime.parse(dateText, standardFormatter)
                if (lastAuthDate.format(dayOfMonthFormatter) == currentDateText.format(dayOfMonthFormatter)) {
                    Log.d("tag", "recyclerDetails: days are equals")
                    lastSeen.text = "Был(а) в сети в ${lastAuthDate.format(timeOfDayFormatter)}"
                } else if (lastAuthDate.format(dayOfYearFormatter) == currentDateText.format(dayOfYearFormatter)) {
                    Log.d("tag", "recyclerDetails: years are equals")
                    lastSeen.text = "Был(а) в сети ${lastAuthDate.format(dayOfMonthFormatter)}"
                } else {
                    lastSeen.text = "Был(а) в сети ${lastAuthDate.format(dayOfYearFormatter)}"
                }
                onlineIndicator.isVisible = false
                lastSeen.setTextColor(context.getColor(R.color.grey_tab_unselected))
            } else {
                onlineIndicator.isVisible = true
                lastSeen.text = context.getString(R.string.online_string)
                lastSeen.setTextColor(context.getColor(R.color.green))
            }
        }

        override fun companionGroupRecyclerDetails(
            imageView: RoundedImageView,
            name: TextView,
            role: TextView
        ) {
            name.text = username
            role.text = if (tripRole == COMPANION) "Попутчик" else "Водитель"
        }

        override fun chatInfo() = ChatInfo(null, username, image, online, lastAuth, username)

        override fun provideId() = userId

        override fun provideName() = username

        override fun provideImage() = image

        companion object {
            private const val COMPANION = "Companion"
            private const val DRIVER = "DRIVER"
        }
    }
}