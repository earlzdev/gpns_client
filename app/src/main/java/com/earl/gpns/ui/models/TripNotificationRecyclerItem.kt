package com.earl.gpns.ui.models

import com.earl.gpns.core.Same

data class TripNotificationRecyclerItemUi(
    val id: String,
    val authorName: String,
    val receiverName: String,
    val authorTripRole: String,
    val receiverTripRole: String,
    val isInvite: Int,
    val timestamp: String,
    var read: Int
) : Same<TripNotificationRecyclerItemUi> {
    override fun same(value: TripNotificationRecyclerItemUi) = this == value
}