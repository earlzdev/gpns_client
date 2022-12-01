package com.earl.gpns.data.retrofit

import com.earl.gpns.data.retrofit.requests.*
import com.earl.gpns.data.retrofit.responses.RoomResponse
import com.earl.gpns.data.retrofit.responses.TokenResponse
import com.earl.gpns.data.retrofit.responses.UserResponse
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
}