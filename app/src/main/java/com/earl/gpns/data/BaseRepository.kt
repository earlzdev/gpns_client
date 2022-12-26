package com.earl.gpns.data

import android.util.Log
import com.earl.gpns.core.AuthResultListener
import com.earl.gpns.data.mappers.*
import com.earl.gpns.data.models.*
import com.earl.gpns.data.models.remote.*
import com.earl.gpns.data.models.remote.requests.*
import com.earl.gpns.data.models.remote.responses.TypingMessageDtoResponse
import com.earl.gpns.data.retrofit.Service
import com.earl.gpns.domain.Repository
import com.earl.gpns.domain.mappers.*
import com.earl.gpns.domain.models.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
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
    private val messageDataToDomainMapper: MessageDataToDomainMapper<MessageDomain>,
    private val typingMessageDomainToDataMapper: TypingMessageDtoDomainToDataMapper<TypingMessageDtoData>,
    private val typingMessageDataToResponseMapper: TypingMessageDataToResponseMapper<TypingMessageDtoResponse>,
    private val groupRemoteToDataMapper: GroupRemoteToDataMapper<GroupData>,
    private val groupDataToDomainMapper: GroupDataToDomainMapper<GroupDomain>,
    private val groupMessageRemoteToDataMapper: GroupMessageRemoteToDataMapper<GroupMessageData>,
    private val groupMessageDataToDomainMapper: GroupMessageDataToDomainMapper<GroupMessageDomain>,
    private val groupTypingMessageDomainToDataMapper: GroupTypingStatusDomainToDataMapper<GroupTypingStatusData>,
    private val groupTypingStatusDataToRequestMapper: GroupTypingStatusDataToRequestMapper<TypingStatusInGroupRequest>,
    private val driverFormDomainToDataMapper: DriverFormDomainToDataMapper<DriverFormData>,
    private val driverFormDataToRemoteMapper: DriverFormDataToRemoteMapper<DriverFormRemote>,
    private val driverFormRemoteToDataMapper: DriverFormRemoteToDataMapper<DriverFormData>,
    private val driverFormDataToDomainMapper: DriverFormDataToDomainMapper<DriverFormDomain>,
    private val companionFormDomainToDataMapper: CompanionFormDomainToDataMapper<CompanionFormData>,
    private val companionFormDataToRemoteMapper: CompanionFormDataToRemoteMapper<CompanionFormRemote>,
    private val companionFormRemoteToDataMapper: CompanionFormRemoteToDataMapper<CompanionFormData>,
    private val companionFormDataToDomainMapper: CompanionFormDataToDomainMapper<CompanionFormDomain>,
    private val companionFormDetailsRemoteToDataMapper: CompanionTripFormDetailsRemoteToDataMapper<CompanionFormDetailsData>,
    private val driverFormDetailsRemoteToDataMapper: DriverTripFormDetailsRemoteToDataMapper<DriverFormDetailsData>,
    private val tripFormDataToDomainMapper: TripFormDataToDomainMapper<TripFormDomain>,
    private val tripNotificationDomainToDataMapper: TripNotificationDomainToDataMapper<TripNotificationData>,
    private val tripNotificationDataToRemoteMapper: TripNotificationDataToRemoteMapper<TripNotificationRemote>
) : Repository {

    override suspend fun register(registerRequest: RegisterRequest, callback: AuthResultListener) {
        try {
            val registerOperationResult = service.register(registerRequest)
            if (registerOperationResult == KEY_SUCCESS) {
                login(
                    LoginRequest(
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
            Log.d("tag", "login: token -> ${token.token}")
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
            val result =  service.fetchUserInfo("Bearer $token")
                .map(userResponseToDataMapper)
                .map(userDataToDomainMapper)
            result
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

    override suspend fun addNewRoom(token: String, newRoomRequest: NewRoomDtoDomain) {
        try {
            service.addRoom("Bearer $token", newRoomRequest.map(newRoomDomainToDataMapper).mapToRequest(newRoomDataToRequestMapper))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun removeRoom(token: String, roomId: String) {
        try {
            service.deleteRoom("Bearer $token", RoomTokenRequest(roomId))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun markMessagesAsRead(token: String, roomId: String) {
        try {
            service.markMessagesAsRead("Bearer $token", RoomTokenRequest(roomId))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun markAuthoredMessageAsRead(token: String, roomId: String, authorName: String) {
        try {
            service.markAuthoredMessagesAsRead("Bearer $token", MarkAuthoredMessageAsReadRequest(roomId, authorName))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun updateLastMsgReadState(token: String, roomId: String) {
        try {
            service.updateLastMsgReadState("Bearer $token", RoomTokenRequest(roomId))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun sendTypingMessageRequest(token: String, request: TypingMessageDtoDomain) {
        try {
            service.typingMessageRequest(
                "Bearer $token",
                request.map(typingMessageDomainToDataMapper).map(typingMessageDataToResponseMapper)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun fetchGroups(token: String) =
        try {
            service.fetchGroups("Bearer $token").map {
                it.map(groupRemoteToDataMapper)
            }.map {
                it.mapToDomain(groupDataToDomainMapper)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }

    override suspend fun fetchMessagesForGroup(
        token: String,
        groupId: String
    ): List<GroupMessageDomain> {
        return try {
            service.fetchMessagesForGroup("Bearer $token", GroupIdRequest(groupId))
                .map { it.map(groupMessageRemoteToDataMapper) }
                .map { it.mapToDomain(groupMessageDataToDomainMapper) }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun sendTypingMessageStatusInGroup(
        token: String,
        request: GroupTypingStatusDomain
    ) {
        try {
            service.sendGroupTypingStatus(
                "Bearer $token",
                request
                    .map(groupTypingMessageDomainToDataMapper)
                    .map(groupTypingStatusDataToRequestMapper)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun markMessagesAsReadInGroup(token: String, groupId: String) {
        try {
            service.markMessagesAsReadInGroup("Bearer $token", GroupIdRequest(groupId))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun sendNewDriverForm(token: String, driverForm: DriverFormDomain) {
        Log.d("tag", "sendNewDriverForm: sent")
        val form = driverForm
            .mapToData(driverFormDomainToDataMapper)
            .mapToRemote(driverFormDataToRemoteMapper)
        try {
            service.sendNewDriverForm(
                "Bearer $token",
                form
            )
        } catch (e: Exception) {
            Log.d("tag", "sendNewDriverForm: $e")
            e.printStackTrace()
        }
    }

    override suspend fun sendNewCompanionForm(token: String, companionForm: CompanionFormDomain) {
        try {
            service.sendNewCompanionForm(
                "Bearer $token",
                companionForm
                    .mapToData(companionFormDomainToDataMapper)
                    .mapToRemote(companionFormDataToRemoteMapper)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun fetchAllTripForms(token: String) : List<TripFormDomain> {
        return try {
            val list = service.fetchAllCompForms("Bearer $token")
            Log.d("tag", "fetchAllTripForms: remote $list")
            val dataList = mutableListOf<TripFormData>()
            for (i in list.indices) {
                dataList.add(TripFormData.Base(
                    list[i].username,
                    list[i].userImage,
                    list[i].companionRole,
                    list[i].from,
                    list[i].to,
                    list[i].schedule,
                    if (list[i].companionRole == COMPANION_ROLE) Json.decodeFromString<CompanionTripFormDetailsRemote>(list[i].details).map(companionFormDetailsRemoteToDataMapper)
                    else Json.decodeFromString<DriverTripFormDetailsRemote>(list[i].details).map(driverFormDetailsRemoteToDataMapper)
                ))
            }
//            Log.d("tag", "fetchAllTripForms: data -> $dataList")
            val domainList = mutableListOf<TripFormDomain>()
            for (i in dataList.indices) {
                domainList.add(dataList[i].map(tripFormDataToDomainMapper))
            }
//            Log.d("tag", "fetchAllTripForms: ready -> $domainList")
            domainList
        } catch (e: Exception) {
            Log.d("tag", "fetchAllTripForms: $e")
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun inviteDriver(token: String, notification: TripNotificationDomain) {
        try {
            service.inviteDriver(
                "Bearer $token",
                notification
                    .mapToData(tripNotificationDomainToDataMapper)
                    .mapToRemote(tripNotificationDataToRemoteMapper)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun inviteCompanion(token: String, notification: TripNotificationDomain) {
        try {
            service.inviteCompanion(
                "Bearer $token",
                notification
                    .mapToData(tripNotificationDomainToDataMapper)
                    .mapToRemote(tripNotificationDataToRemoteMapper)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val KEY_SUCCESS = "success"
        private const val COMPANION_ROLE = "COMPANION_ROLE"
        private const val DRIVER_ROLE = "DRIVER_ROLE"
    }
}