package com.earl.gpns.domain.webSocketActions.services

import com.earl.gpns.domain.models.GroupDomain

interface AddNewGroup {
    fun addNewGroup(group: GroupDomain)
}