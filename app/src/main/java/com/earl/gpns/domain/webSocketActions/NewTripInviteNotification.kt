package com.earl.gpns.domain.webSocketActions

import com.earl.gpns.domain.models.TripNotificationDomain

interface NewTripInviteNotification {
    fun reactOnNewNotification(notification: TripNotificationDomain)
}