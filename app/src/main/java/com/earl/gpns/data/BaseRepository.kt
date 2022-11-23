package com.earl.gpns.data

import com.earl.gpns.core.AuthResultListener
import com.earl.gpns.core.OperationResultListener
import com.earl.gpns.data.retrofit.Service
import com.earl.gpns.data.retrofit.requests.LoginRequest
import com.earl.gpns.data.retrofit.requests.RegisterRequest
import com.earl.gpns.domain.Repository
import retrofit2.HttpException
import javax.inject.Inject

class BaseRepository @Inject constructor(
    private val service: Service
) : Repository {

    override suspend fun register(registerRequest: RegisterRequest, callback: AuthResultListener) {
        try {
            val registerOperationResult = service.register(registerRequest)
            if (registerOperationResult == KEY_SUCCESS) {
                login(LoginRequest(
                    registerRequest.email,
                    registerRequest.password
                ), callback)
            } else {
                callback.unknownError(Exception(registerOperationResult))
            }
        } catch (e: HttpException) {
            e.printStackTrace()
            callback.unauthorized(e)
        } catch (e: Exception) {
            e.printStackTrace()
            callback.unknownError(e)
        }
    }

    override suspend fun login(loginRequest: LoginRequest, callback: AuthResultListener) {
        try {
            val token = service.login(loginRequest)
            callback.authorized(token.token)
        } catch (e: HttpException) {
            e.printStackTrace()
            callback.unauthorized(e)
        } catch (e: Exception) {
            callback.unknownError(e)
        }
    }

    override suspend fun authenticate(token: String, callback: AuthResultListener) {
        try {
//            val authenticateResult = service.authenticate("Bearer $token")
//            if (authenticateResult == KEY_SUCCESS) {
//                callback.authorized(authenticateResult)
//            } else {
//                callback.unknownError(Exception(authenticateResult))
//            }
            service.authenticate("Bearer $token")
            callback.authorized(KEY_SUCCESS)
        } catch (e: HttpException) {
            callback.unauthorized(e)
        } catch (e: Exception) {
            callback.unknownError(e)
        }
    }

    override suspend fun getSecretInfo(token: String, callback: OperationResultListener) {
        try {
            val response = service.getSecretInfo("Bearer $token")
            callback.success(response.token)
        } catch (e: HttpException) {
            e.printStackTrace()
            callback.fail(e)
        } catch (e: Exception) {
            e.printStackTrace()
            callback.fail(e)
        }
    }

    companion object {
        private const val KEY_SUCCESS = "success"
    }
}