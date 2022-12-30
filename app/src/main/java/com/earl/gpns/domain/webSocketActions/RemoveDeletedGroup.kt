package com.earl.gpns.domain.webSocketActions

import com.earl.gpns.domain.models.GroupDomain

interface RemoveDeletedGroup {
    fun removeDeletedGroup(group: GroupDomain)
}