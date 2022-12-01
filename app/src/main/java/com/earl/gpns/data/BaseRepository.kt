package com.earl.gpns.data

import com.earl.gpns.core.AuthResultListener
import com.earl.gpns.data.mappers.*
import com.earl.gpns.data.models.MessageData
import com.earl.gpns.data.models.NewRoomDtoData
import com.earl.gpns.data.models.RoomData
import com.earl.gpns.data.models.UserData
import com.earl.gpns.data.retrofit.Service
import com.earl.gpns.data.retrofit.requests.LoginRequest
import com.earl.gpns.data.retrofit.requests.NewRoomRequest
import com.earl.gpns.data.retrofit.requests.RegisterRequest
import com.earl.gpns.data.retrofit.requests.RoomTokenRequest
import com.earl.gpns.domain.repositories.Repository
import com.earl.gpns.domain.mappers.NewRoomDomainToDataMapper
import com.earl.gpns.domain.models.MessageDomain
import com.earl.gpns.domain.models.RoomDomain
import com.earl.gpns.domain.models.UserDomain
import retrofit2.HttpException
import javax.inject.Inject

class BaseRepository @Inject constructor(
    private val service: Service,
    private val userResponseToDataMapper: UserResponseToDataMapper<UserData>,
    private val userDataToDomainMapper: UserDataToDomainMapper<UserDomain>,
    private val roomResponseToDataMapper: RoomResponseToDataMapper<RoomData>,
    private val roomDataToDomainMapper: RoomDataToDomainMapper<RoomDomain>,
    private val newRoomDomainToDataMapper: NewRoomDomainToDataMapper<NewRoomDtoData>,
    private val newRoomDataToRequestMapper: NewRoomDataToRequestMapper<NewRoomRequest>,
    private val messageRemoteToDataMapper: MessageRemoteToDataMapper<MessageData>,
    private val messageDataToDomainMapper: MessageDataToDomainMapper<MessageDomain>
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
            service.authenticate("Bearer $token")
            callback.authorized(KEY_SUCCESS)
        } catch (e: HttpException) {
            callback.unauthorized(e)
        } catch (e: Exception) {
            callback.unknownError(e)
        }
    }

    override suspend fun fetchUsers(token: String): List<UserDomain> {
        return try {
            val list = service.users("Bearer $token")
                .map { it.map(userResponseToDataMapper) }
                .map { it.map(userDataToDomainMapper) }
            list
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun fetchRooms(token: String): List<RoomDomain> {
        return try {
            service.rooms("Bearer $token")
                .map { it.map(roomResponseToDataMapper) }
                .map { it.map(roomDataToDomainMapper) }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun fetchUserInfo(token: String): UserDomain? {
        return try {
            service.fetchUserInfo("Bearer $token")
                .map(userResponseToDataMapper)
                .map(userDataToDomainMapper)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun fetchMessagesForRoom(token: String, roomId: String): List<MessageDomain> {
        return try {
            val list = service.fetchMessagesForRoom("Bearer $token", RoomTokenRequest(roomId))
            list
                .map { it.map(messageRemoteToDataMapper) }
                .map { it.mapToDomain(messageDataToDomainMapper) }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun removeRoom(token: String, roomId: String) {
        try {
            service.deleteRoom("Bearer $token", RoomTokenRequest(roomId))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val KEY_SUCCESS = "success"
    }
}