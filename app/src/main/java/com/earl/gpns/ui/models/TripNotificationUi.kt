package com.earl.gpns.ui.models

import com.earl.gpns.ui.core.Same
import com.earl.gpns.ui.mappers.TripNotificationUiToDomainMapper

interface TripNotificationUi : Same<TripNotificationUi> {

    fun <T> mapToDomain(mapper: TripNotificationUiToDomainMapper<T>) : T

    override fun same(value: TripNotificationUi) = this == value

    fun provideTripNotificationUiRecyclerItem() : TripNotificationRecyclerItemUi

    fun provideId() : String

    fun provideReceiverName() : String

    fun provideAuthorName() : String

    fun isActive() : Boolean

    class Base(
        private val id: String,
        private val authorName: String,
        private val receiverName: String,
        private val authorTripRole: String,
        private val receiverTripRole: String,
        private val type: String,
        private val timestamp: String,
        private val active: Int
    ) : TripNotificationUi {

        override fun <T> mapToDomain(mapper: TripNotificationUiToDomainMapper<T>) =
            mapper.map(id, authorName, receiverName, authorTripRole, receiverTripRole, type, timestamp, active)

        override fun provideTripNotificationUiRecyclerItem() = TripNotificationRecyclerItemUi(
            id, authorName, receiverName, authorTripRole, receiverTripRole, type, timestamp, 0, if (type == INVITE) 1 else 0, active
        )

        override fun provideId() = id

        override fun provideReceiverName() = receiverName

        override fun provideAuthorName() = authorName

        override fun isActive() = active == IS_ACTIVE
    }

    companion object {
        private const val INVITE = "INVITE"
        private const val IS_ACTIVE = 1
    }
}