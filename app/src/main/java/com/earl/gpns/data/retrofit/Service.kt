package com.earl.gpns.data.retrofit

import com.earl.gpns.data.retrofit.requests.LoginRequest
import com.earl.gpns.data.retrofit.requests.RegisterRequest
import com.earl.gpns.data.retrofit.responses.TokenResponse
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
    @GET("/getSecretInfo")
    suspend fun getSecretInfo(
        @Header("Authorization") token: String
    ) : TokenResponse
}