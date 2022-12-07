package com.earl.gpns.domain.repositories

import com.earl.gpns.core.AuthResultListener
import com.earl.gpns.core.OperationResultListener
import com.earl.gpns.data.models.remote.requests.LoginRequest
import com.earl.gpns.data.models.remote.requests.NewRoomRequest
import com.earl.gpns.data.models.remote.requests.RegisterRequest
import com.earl.gpns.data.models.remote.responses.RoomResponse
import com.earl.gpns.domain.models.MessageDomain
import com.earl.gpns.domain.models.NewRoomDtoDomain
import com.earl.gpns.domain.models.RoomDomain
import com.earl.gpns.domain.models.UserDomain

interface Repository {

    suspend fun register(registerRequest: RegisterRequest, callback: AuthResultListener)

    suspend fun login(loginRequest: LoginRequest, callback: AuthResultListener)

    suspend fun authenticate(token: String, callback: AuthResultListener)

    suspend fun fetchUsers(token: String) : List<UserDomain>

    suspend fun fetchRooms(token: String) : List<RoomDomain>

    suspend fun fetchUserInfo(token: String) : UserDomain?

    suspend fun fetchMessagesForRoom(token: String, roomId: String) : List<MessageDomain>

    suspend fun removeRoom(token: String, roomId: String)

    suspend fun markMessagesAsRead(token: String, roomId: String)
}