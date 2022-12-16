package com.earl.gpns.domain.webSocketActions.services

import com.earl.gpns.domain.webSocketActions.RemoveDeletedByAnotherUserRoomFromDb
import com.earl.gpns.domain.webSocketActions.UpdateLastMessageReadStateInRoom
import com.earl.gpns.domain.webSocketActions.UpdateLastMessageInRoom
import com.earl.gpns.domain.webSocketActions.UpdateUserOnlineInRoom

interface RoomsObservingSocketService :
        RemoveDeletedByAnotherUserRoomFromDb,
        UpdateLastMessageReadStateInRoom,
        UpdateLastMessageInRoom,
        UpdateUserOnlineInRoom