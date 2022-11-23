package com.earl.gpns.domain

import com.earl.gpns.core.AuthResultListener
import com.earl.gpns.core.OperationResultListener
import com.earl.gpns.data.retrofit.requests.LoginRequest
import com.earl.gpns.data.retrofit.requests.RegisterRequest

interface Repository {

    suspend fun register(registerRequest: RegisterRequest, callback: AuthResultListener)

    suspend fun login(loginRequest: LoginRequest, callback: AuthResultListener)

    suspend fun authenticate(token: String, callback: AuthResultListener)

    suspend fun getSecretInfo(token: String, callback: OperationResultListener)
}