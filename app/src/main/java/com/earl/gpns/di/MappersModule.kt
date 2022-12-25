package com.earl.gpns.di

import com.earl.gpns.data.localDb.RoomDb
import com.earl.gpns.data.mappers.*
import com.earl.gpns.data.models.*
import com.earl.gpns.data.models.remote.CompanionFormRemote
import com.earl.gpns.data.models.remote.DriverFormRemote
import com.earl.gpns.data.models.remote.GroupMessageRemote
import com.earl.gpns.data.models.remote.MessageRemote
import com.earl.gpns.data.models.remote.requests.NewRoomRequest
import com.earl.gpns.data.models.remote.requests.TypingStatusInGroupRequest
import com.earl.gpns.data.models.remote.responses.TypingMessageDtoResponse
import com.earl.gpns.domain.mappers.*
import com.earl.gpns.domain.models.*
import com.earl.gpns.ui.mappers.*
import com.earl.gpns.ui.models.*
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

    @Provides
    @Singleton
    fun provideNewLastMsgResponseToDataMapper() : NewLastMsgResponseToDataMapper<NewLastMessageInRoomData> {
        return BaseLastMsgResponseToDataMapper()
    }

    @Provides
    @Singleton
    fun provideNewLastMsgDataToDomainMapper() : NewLastMsgDataToDomainMapper<NewLastMessageInRoomDomain> {
        return BaseLastMsgDataToDomainMapper()
    }

    @Provides
    @Singleton
    fun provideNewLastMsgDomainToUiMapper() : NewLastMessageInRoomDomainToUiMapper<NewLastMessageInRoomUi> {
        return BaseNewLastMessageDomainToUiMapper()
    }

    @Provides
    @Singleton
    fun provideTypingMessageDtoUiToDomainMapper() : TypingMessageDtoUiToDomainMapper<TypingMessageDtoDomain> {
        return BaseTypingMessageUiToDomainMapper()
    }

    @Provides
    @Singleton
    fun provideTypingMessageDtoDomainToDataMapper() : TypingMessageDtoDomainToDataMapper<TypingMessageDtoData> {
        return BaseTypingMessageDtoDomainToDataMapper()
    }

    @Provides
    @Singleton
    fun provideTypingMessageDtoDataToResponseMapper() : TypingMessageDataToResponseMapper<TypingMessageDtoResponse> {
        return BaseTypingMessageDataToResponseMapper()
    }

    @Provides
    @Singleton
    fun provideGroupRemoteToDataMapper() : GroupRemoteToDataMapper<GroupData> {
        return BaseGroupRemoteToDataMapper()
    }

    @Provides
    @Singleton
    fun provideGroupDataToDomainMapper() : GroupDataToDomainMapper<GroupDomain> {
        return BaseGroupDataToDomainMapper()
    }

    @Provides
    @Singleton
    fun provideGroupDomainToUiMapper() : GroupDomainToUiMapper<GroupUi> {
        return BaseGroupDomainToUiMapper()
    }

    @Provides
    @Singleton
    fun provideGroupMessageRemoteToDataMapper() : GroupMessageRemoteToDataMapper<GroupMessageData> {
        return BaseGroupMessageRemoteToDataMapper()
    }

    @Provides
    @Singleton
    fun provideGroupMessageDataToDomainMapper() : GroupMessageDataToDomainMapper<GroupMessageDomain> {
        return BaseGroupMessageDataToDomainMapper()
    }

    @Provides
    @Singleton
    fun provideGroupMessageDomainToUiMapper() : GroupMessageDomainToUiMapper<GroupMessageUi> {
        return BaseGroupMessageDomainToUiMapper()
    }

    @Provides
    @Singleton
    fun provideGroupMessageUiToDomainMapper() : GroupMessageUiToDomainMapper<GroupMessageDomain> {
        return BaseGroupMessageUiToDomainMapper()
    }

    @Provides
    @Singleton
    fun provideGroupMessageDomainToDataMapper() : GroupMessageDomainToDataMapper<GroupMessageData> {
        return BaseGroupMessageDomainToDataMapper()
    }

    @Provides
    @Singleton
    fun provideGroupMessageDataToRemoteMapper() : GroupMessageDataToRemoteMapper<GroupMessageRemote> {
        return BaseGroupMessageDataToRemoteMapper()
    }

    @Provides
    @Singleton
    fun provideGroupLastMessageResponseToDataMapper() : GroupLastMessageResponseToDataMapper<GroupLastMessageData> {
        return BaseGroupLastMessageResponseToDataMapper()
    }

    @Provides
    @Singleton
    fun provideGroupLastMessageDataToDomainMapper() : GroupLastMessageDataToDomainMapper<GroupLastMessageDomain> {
        return BaseGroupLastMessageDataToDomainMapper()
    }

    @Provides
    @Singleton
    fun provideGroupLastMessageDomainToUiMapper() : GroupLastMessageDomainToUiMapper<GroupLastMessageUi> {
        return BaseGroupLastMessageDomainToUiMapper()
    }

    @Provides
    @Singleton
    fun provideGroupTypingStatusUiToDomainMapper() : GroupTypingStatusUiToDomainMapper<GroupTypingStatusDomain> {
        return BaseGroupTypingStatusUiToDomainMapper()
    }

    @Provides
    @Singleton
    fun provideGroupTypingStatusDomainToDataMapper() : GroupTypingStatusDomainToDataMapper<GroupTypingStatusData> {
        return BaseGroupTypingStatusDomainToDataMapper()
    }

    @Provides
    @Singleton
    fun provideGroupTypingStatusDataToRequestMapper() : GroupTypingStatusDataToRequestMapper<TypingStatusInGroupRequest> {
        return BaseGroupTypingStatusDataToRequestMapper()
    }

    @Provides
    @Singleton
    fun provideGroupTypingStatusRemoteToDataMapper() : GroupTypingStatusRemoteToDataMapper<GroupTypingStatusData> {
        return BaseGroupTypingStatusRemoteToDataMapper()
    }

    @Provides
    @Singleton
    fun provideGroupTypingStatusDataToDomainMapper() : GroupTypingStatusDataToDomainMapper<GroupTypingStatusDomain> {
        return BaseGroupTypingStatusDataToDomainMapper()
    }

    @Provides
    @Singleton
    fun provideGroupTypingStatusDomainToUiMapper() : GroupTypingStatusDomainToUiMapper<GroupTypingStatusUi> {
        return BaseGroupTypingStatusDomainToUiMapper()
    }

    @Provides
    @Singleton
    fun provideGroupMessagesCounterDbToDataMapper() : GroupMessagesCounterDbToDataMapper<GroupMessagesCounterData> {
        return BaseGroupMessagesCounterDbToDataMapper()
    }

    @Provides
    @Singleton
    fun provideGroupMessagesCounterDataToDomainMapper() : GroupMessagesCounterDataToDomainMapper<GroupMessagesCounterDomain> {
        return BaseGroupMessagesCounterDataToDomainMapper()
    }

    @Provides
    @Singleton
    fun provideGroupMessagesCounterDomainToUiMapper() : GroupMessagesCounterDomainToUimapper<GroupMessagesCounterUi> {
        return BaseGroupMessagesCounterDomainToUiMapper()
    }

    @Provides
    @Singleton
    fun provideDriverFormRemoteToDataMapper() : DriverFormRemoteToDataMapper<DriverFormData> {
        return BaseDriverFormRemoteToDataMapper()
    }

    @Provides
    @Singleton
    fun provideDriverFormDataToDomainMapper() : DriverFormDataToDomainMapper<DriverFormDomain> {
        return BaseDriverFormDataToDomainMapper()
    }

    @Provides
    @Singleton
    fun provideDriverFormDomainToUiMapper() : DriverFormDomainToUiMapper<DriverFormUi> {
        return BaseDriverFormDomainToUiMapper()
    }

    @Provides
    @Singleton
    fun provideDriverFormUiToDomainMapper() : DriverFormUiToDomainMapper<DriverFormDomain> {
        return BaseDriverFormUiToDomainMapper()
    }

    @Provides
    @Singleton
    fun provideDriverFormDomainToDataMapper() : DriverFormDomainToDataMapper<DriverFormData> {
        return BaseDriverFormDomainToDataMapper()
    }

    @Provides
    @Singleton
    fun provideDriverFormDataToRemoteMapper() : DriverFormDataToRemoteMapper<DriverFormRemote> {
        return BaseDriverFormDataToRemoteMapper()
    }

    @Provides
    @Singleton
    fun provideCompanionFormUiToDomainMapper() : CompanionFormUiToDomainMapper<CompanionFormDomain> {
        return BaseCompanionFormUiToDomainMapper()
    }

    @Provides
    @Singleton
    fun provideCompanionFormDomainToDataMapper() : CompanionFormDomainToDataMapper<CompanionFormData> {
        return BaseCompanionFormDomainToDataMapper()
    }

    @Provides
    @Singleton
    fun provideCompanionFormDataToRemoteMapper() : CompanionFormDataToRemoteMapper<CompanionFormRemote> {
        return BaseCompanionFormDataToRemoteMapper()
    }

    @Provides
    @Singleton
    fun provideCompanionFormRemoteToDataMapper() : CompanionFormRemoteToDataMapper<CompanionFormData> {
        return BaseCompanionFormRemoteToDataMapper()
    }

    @Provides
    @Singleton
    fun provideCompanionFormDataToDomainMapper() : CompanionFormDataToDomainMapper<CompanionFormDomain> {
        return BaseCompanionFormDataToDomainMapper()
    }

    @Provides
    @Singleton
    fun provideCompanionFormDomainToUiMapper() : CompanionFormDomainToUiMapper<CompanionFormUi> {
        return BaseCompanionFormDomainToUiMapper()
    }

    @Provides
    @Singleton
    fun provideCompanionFormDetailsRemoteToDataMapper() : CompanionTripFormDetailsRemoteToDataMapper<CompanionFormDetailsData> {
        return BaseCompanionFormDetailsRemoteToDataMapper()
    }

    @Provides
    @Singleton
    fun provideCompanionFormDetailsDataToDomainMapper() : CompanionFormDetailsDataToDomainMapper<CompanionFormDetailsDomain> {
        return BaseCompanionFormDetailsDataToDomainMapper()
    }

    @Provides
    @Singleton
    fun provideDriverFormDetailsRemoteToDataMapper() : DriverTripFormDetailsRemoteToDataMapper<DriverFormDetailsData> {
        return BaseDriverFormDetailsRemoteToDataMapper()
    }

    @Provides
    @Singleton
    fun provideDriverFormDetailsDataToDomainMapper() : DriverFormDetailsDataToDomainMapper<DriverFormDetailsDomain> {
        return BaseDriverFormDetailsDataToDomainMapper()
    }

    @Provides
    @Singleton
    fun provideTripFormDataToDomainMapper(
        driverFormDetailsDataToDomainMapper: DriverFormDetailsDataToDomainMapper<DriverFormDetailsDomain>,
        companionFormDetailsDataToDomainMapper: CompanionFormDetailsDataToDomainMapper<CompanionFormDetailsDomain>
    ) : TripFormDataToDomainMapper<TripFormDomain> {
        return BaseTripFormDataToDomainMapper(
            driverFormDetailsDataToDomainMapper,
            companionFormDetailsDataToDomainMapper
        )
    }

    @Provides
    @Singleton
    fun provideDriverFormDetailsDomainToUiMapper() : DriverFormDetailsDomainToUiMapper<DriverFormDetailsUi> {
        return BaseDriverFormDetailsDomainToUiMapper()
    }

    @Provides
    @Singleton
    fun provideCompanionFormDetailsDomainToUiMapper() : CompanionFormDetailsDomainToUiMapper<CompanionFormDetailsUi> {
        return BaseCompanionFormDetailsDomainToUiMapper()
    }

    @Provides
    @Singleton
    fun provideTripFormDomainToUiMapper(
        driverFormDetailsDomainToUiMapper: DriverFormDetailsDomainToUiMapper<DriverFormDetailsUi>,
        companionFormDetailsDomainToUiMapper: CompanionFormDetailsDomainToUiMapper<CompanionFormDetailsUi>
    ) : TripFormDomainToUiMapper<TripFormUi> {
        return BaseTripFormDomainToUiMapper(
            driverFormDetailsDomainToUiMapper,
            companionFormDetailsDomainToUiMapper
        )
    }
}

