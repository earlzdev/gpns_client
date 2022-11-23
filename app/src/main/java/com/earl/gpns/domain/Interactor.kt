package com.earl.gpns.domain

import com.earl.gpns.core.AuthResultListener
import com.earl.gpns.core.OperationResultListener
import com.earl.gpns.data.retrofit.requests.LoginRequest
import com.earl.gpns.data.retrofit.requests.RegisterRequest
import javax.inject.Inject

interface Interactor {

    suspend fun register(registerRequest: RegisterRequest, callback: AuthResultListener)

    suspend fun login(loginRequest: LoginRequest, callback: AuthResultListener)

    suspend fun authenticate(token: String, callback: AuthResultListener)

    suspend fun getSecretInfo(token: String, callback: OperationResultListener)

    class Base @Inject constructor(
        private val repository: Repository
    ) : Interactor {

        override suspend fun register(registerRequest: RegisterRequest, callback: AuthResultListener) {
            repository.register(registerRequest, callback)
        }

        override suspend fun login(loginRequest: LoginRequest, callback: AuthResultListener) {
            repository.login(loginRequest, callback)
        }

        override suspend fun authenticate(token: String, callback: AuthResultListener) {
            repository.authenticate(token, callback)
        }

        override suspend fun getSecretInfo(token: String, callback: OperationResultListener) {
            repository.getSecretInfo(token, callback)
        }
    }
}