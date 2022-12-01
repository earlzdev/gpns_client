package com.earl.gpns.di

import com.earl.gpns.data.local.RoomDb
import com.earl.gpns.data.mappers.*
import com.earl.gpns.data.models.MessageData
import com.earl.gpns.data.models.NewRoomDtoData
import com.earl.gpns.data.models.RoomData
import com.earl.gpns.data.models.UserData
import com.earl.gpns.data.retrofit.requests.MessageRemote
import com.earl.gpns.data.retrofit.requests.NewRoomRequest
import com.earl.gpns.domain.mappers.*
import com.earl.gpns.domain.models.MessageDomain
import com.earl.gpns.domain.models.NewRoomDtoDomain
import com.earl.gpns.domain.models.RoomDomain
import com.earl.gpns.domain.models.UserDomain
import com.earl.gpns.ui.mappers.*
import com.earl.gpns.ui.models.MessageUi
import com.earl.gpns.ui.models.NewRoomDtoUi
import com.earl.gpns.ui.models.RoomUi
import com.earl.gpns.ui.models.UserUi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MappersModule {

    @Provides
    @Singleton
    fun provideUserResponseToDataMapper() : UserResponseToDataMapper<UserData> {
        return BaseUserResponseToDataMapper()
    }

    @Provides
    @Singleton
    fun provideUserDataToDomainMapper() : UserDataToDomainMapper<UserDomain> {
        return BaseUserDataToDomainMapper()
    }

    @Provides
    @Singleton
    fun provideUserDomainToUiMapper() : UserDomainToUiMapper<UserUi> {
        return BaseUserDomainToUiMapper()
    }

    @Provides
    @Singleton
    fun provideRoomResponseToDataMapper() : RoomResponseToDataMapper<RoomData> {
        return BaseRoomResponseToDataMapper()
    }

    @Provides
    @Singleton
    fun provideRoomDataToDomainMapper() : RoomDataToDomainMapper<RoomDomain> {
        return BaseRoomDataToDomainMapper()
    }

    @Provides
    @Singleton
    fun provideRoomDomainToUiMapper() : RoomDomainToUiMapper<RoomUi> {
        return BaseRoomDomainToUiMapper()
    }

    @Provides
    @Singleton
    fun provideNewRoomUiToDomainMapper() : NewRoomUiToDomainMapper<NewRoomDtoDomain> {
        return BaseNewRoomUiToDomainMapper()
    }

    @Provides
    @Singleton
    fun provideNewRoomDomainToDataMapper() : NewRoomDomainToDataMapper<NewRoomDtoData> {
        return BaseNewRoomDomainToDataMapper()
    }

    @Provides
    @Singleton
    fun provideNewRoomDataToRequestMapper() : NewRoomDataToRequestMapper<NewRoomRequest> {
        return BaseNewRoomDataToRequestMapper()
    }

    @Provides
    @Singleton
    fun provideMessageResponseToDataMapper() : MessageRemoteToDataMapper<MessageData> {
        return BaseMessageRemoteToDataMapper()
    }

    @Provides
    @Singleton
    fun provideMessageDataToDomainMapper() : MessageDataToDomainMapper<MessageDomain> {
        return BaseMessageDataToDomainMapper()
    }

    @Provides
    @Singleton
    fun provideMessageDomainToUiMapper() : MessageDomainToUiMapper<MessageUi> {
        return BaseMessageDomainToUiMapper()
    }

    @Provides
    @Singleton
    fun provideMessageUiToDomainMapper() : MessageUiToDomainMapper<MessageDomain> {
        return BaseMessageUiToDomainMapper()
    }

    @Provides
    @Singleton
    fun provideMessageDomainToDataMapper() : MessageDomainToDataMapper<MessageData> {
        return BaseMessageDomainToDataMapper()
    }

    @Provides
    @Singleton
    fun provideMessageDataToResponseMapper() : MessageDataToRemoteMapper<MessageRemote> {
        return BaseMessageDataToRemoteMapper()
    }

    @Provides
    @Singleton
    fun provideRoomDbToDataMapper() : RoomDbToDataMapper<RoomData> {
        return BaseRoomDbToDataMapper()
    }

    @Provides
    @Singleton
    fun provideRoomDataToDbMapper() : NewRoomDataToDbMapper<RoomDb> {
        return BaseNewRoomDataToDbMapper()
    }

    @Provides
    @Singleton
    fun provideRoomDomainToNewRoomDomainMapper() : RoomDomainToNewRoomDomainMapper<NewRoomDtoDomain> {
        return BaseRoomDomainToNewRoomDomainMapper()
    }
}