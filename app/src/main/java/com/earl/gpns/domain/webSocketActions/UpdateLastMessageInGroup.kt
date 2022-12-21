package com.earl.gpns.domain.webSocketActions

import com.earl.gpns.domain.models.GroupLastMessageDomain

interface UpdateLastMessageInGroup {
    fun updateLastMessageInGroup(newLastMessageDomain: GroupLastMessageDomain)
}