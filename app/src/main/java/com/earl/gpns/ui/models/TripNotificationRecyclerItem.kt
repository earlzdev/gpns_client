package com.earl.gpns.ui.models

import com.earl.gpns.core.Same

data class TripNotificationRecyclerItemUi(
    val id: String,
    val authorName: String,
    val receiverName: String,
    val authorTripRole: String,
    val receiverTripRole: String,
    val type: String,
    val timestamp: String,
    var read: Int,
    var watchable: Int,
    var active: Int
) : Same<TripNotificationRecyclerItemUi> {
    override fun same(value: TripNotificationRecyclerItemUi) = this == value
}
