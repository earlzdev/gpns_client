package com.earl.gpns.data.retrofit

import com.earl.gpns.data.models.remote.GroupMessageRemote
import com.earl.gpns.data.models.remote.GroupRemote
import com.earl.gpns.data.models.remote.MessageRemote
import com.earl.gpns.data.models.remote.requests.*
import com.earl.gpns.data.models.remote.responses.RoomResponse
import com.earl.gpns.data.models.remote.responses.TokenResponse
import com.earl.gpns.data.models.remote.responses.TypingMessageDtoResponse
import com.earl.gpns.data.models.remote.responses.UserResponse
import retrofit2.http.*

interface Service {

    @Headers("Content-Type: application/json")
    @POST("/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ) : String

    @Headers("Content-Type: application/json")
    @POST("/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ) : TokenResponse

    @Headers("Content-Type: application/json")
    @GET("/authenticate")
    suspend fun authenticate(
        @Header("Authorization") token: String
    )

    @Headers("Content-Type: application/json")
    @GET("/fetchUserInfo")
    suspend fun fetchUserInfo(
        @Header("Authorization") token: String
    ) : UserResponse

    @Headers("Content-Type: application/json")
    @GET("/users")
    suspend fun users(
        @Header("Authorization") token: String
    ) : List<UserResponse>

    @Headers("Content-Type: application/json")
    @GET("/fetchRooms")
    suspend fun rooms(
        @Header("Authorization") token: String
    ) : List<RoomResponse>

    @Headers("Content-Type: application/json")
    @POST("/addRoom")
    suspend fun addRoom(
        @Header("Authorization") token: String,
        @Body newRoomRequest: NewRoomRequest
    )

    @Headers("Content-Type: application/json")
    @POST("/fetchMessagesForRoom")
    suspend fun fetchMessagesForRoom(
        @Header("Authorization") token: String,
        @Body roomId: RoomTokenRequest
    ) : List<MessageRemote>

    @Headers("Content-Type: application/json")
    @POST("/deleteRoom")
    suspend fun deleteRoom(
        @Header("Authorization") token: String,
        @Body roomId: RoomTokenRequest
    )

    @Headers("Content-Type: application/json")
    @POST("/markMessagesAsRead")
    suspend fun markMessagesAsRead(
        @Header("Authorization") token: String,
        @Body roomId: RoomTokenRequest
    )

    @Headers("Content-Type: application/json")
    @POST("/markAuthoredMessagesAsRead")
    suspend fun markAuthoredMessagesAsRead(
        @Header("Authorization") token: String,
        @Body request: MarkAuthoredMessageAsReadRequest
    )

    @Headers("Content-Type: application/json")
    @POST("/updateLastMsgReadState")
    suspend fun updateLastMsgReadState(
        @Header("Authorization") token: String,
        @Body request: RoomTokenRequest
    )

    @Headers("Content-Type: application/json")
    @POST("/typingMessageRequest")
    suspend fun typingMessageRequest(
        @Header("Authorization") token: String,
        @Body request: TypingMessageDtoResponse
    )

    @Headers("Content-Type: application/json")
    @GET("/fetchGroups")
    suspend fun fetchGroups(
        @Header("Authorization") token: String
    ) : List<GroupRemote>

    @Headers("Content-Type: application/json")
    @POST("/fetchMessagesForGroup")
    suspend fun fetchMessagesForGroup(
        @Header("Authorization") token: String,
        @Body roomId: GroupIdRequest
    ) : List<GroupMessageRemote>

    @Headers("Content-Type: application/json")
    @POST("/groupTypingStatus")
    suspend fun sendGroupTypingStatus(
        @Header("Authorization") token: String,
        @Body request: TypingStatusInGroupRequest
    )

    @Headers("Content-Type: application/json")
    @POST("/markMessagesAsReadInGroup")
    suspend fun markMessagesAsReadInGroup(
        @Header("Authorization") token: String,
        @Body groupId: GroupIdRequest
    )
}