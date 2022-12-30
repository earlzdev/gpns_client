package com.earl.gpns.domain.webSocketActions.services

import com.earl.gpns.domain.webSocketActions.*

interface RoomsObservingSocketService :
        RemoveDeletedByAnotherUserRoomFromDb,
        UpdateLastMessageReadStateInRoom,
        UpdateLastMessageInRoom,
        UpdateUserOnlineInRoom,
        UpdateLastMessageInGroup,
        MarkAuthoredMessagesAsReadInGroup,
        AddNewGroup,
        RemoveDeletedGroup