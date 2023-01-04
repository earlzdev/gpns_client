package com.earl.gpns.ui.models

import com.earl.gpns.core.Same
import com.earl.gpns.ui.mappers.TripNotificationUiToDomainMapper

interface TripNotificationUi : Same<TripNotificationUi> {

    fun <T> mapToDomain(mapper: TripNotificationUiToDomainMapper<T>) : T

    override fun same(value: TripNotificationUi) = this == value

    fun provideTripNotificationUiRecyclerItem() : TripNotificationRecyclerItemUi

    fun provideId() : String

    fun provideReceiverName() : String

    fun provideAuthorName() : String

    class Base(
        private val id: String,
        private val authorName: String,
        private val receiverName: String,
        private val authorTripRole: String,
        private val receiverTripRole: String,
        private val type: String,
        private val timestamp: String,
    ) : TripNotificationUi {

        override fun <T> mapToDomain(mapper: TripNotificationUiToDomainMapper<T>) =
            mapper.map(id, authorName, receiverName, authorTripRole, receiverTripRole, type, timestamp)

        override fun provideTripNotificationUiRecyclerItem() = TripNotificationRecyclerItemUi(
            id, authorName, receiverName, authorTripRole, receiverTripRole, type, timestamp, 0, if (type == INVITE) 1 else 0
        )

        override fun provideId() = id

        override fun provideReceiverName() = receiverName

        override fun provideAuthorName() = authorName
    }

    companion object {
        private const val INVITE = "INVITE"
    }
}

/*
notification isInvite
0 - отказ - не исп
1 - приглашение
2 - водитель удалил анкету
3 - согласие
4 - отказ - не исп
 */