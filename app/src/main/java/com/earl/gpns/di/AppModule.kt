package com.earl.gpns.di

import android.content.Context
import androidx.room.Room
import com.earl.gpns.data.BaseDatabaseRepository
import com.earl.gpns.data.BaseRepository
import com.earl.gpns.data.BaseSocketRepository
import com.earl.gpns.data.SocketActionsParser
import com.earl.gpns.data.localDb.*
import com.earl.gpns.data.mappers.*
import com.earl.gpns.data.models.*
import com.earl.gpns.data.models.remote.*
import com.earl.gpns.data.models.remote.requests.NewRoomRequest
import com.earl.gpns.data.models.remote.requests.TypingStatusInGroupRequest
import com.earl.gpns.data.models.remote.responses.TypingMessageDtoResponse
import com.earl.gpns.data.retrofit.Service
import com.earl.gpns.domain.DatabaseRepository
import com.earl.gpns.domain.Interactor
import com.earl.gpns.domain.Repository
import com.earl.gpns.domain.SocketsRepository
import com.earl.gpns.domain.mappers.*
import com.earl.gpns.domain.models.*
import com.earl.gpns.ui.models.GroupLastMessageUi
import com.earl.gpns.ui.models.GroupUi
import com.earl.gpns.ui.models.NewLastMessageInRoomUi
import com.earl.gpns.ui.rooms.RoomsObservingSocketController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRepository(
        service: Service,
        userResponseToDataMapper: UserResponseToDataMapper<UserData>,
        userDataToDomainMapper: UserDataToDomainMapper<UserDomain>,
        roomResponseToDataMapper: RoomResponseToDataMapper<RoomData>,
        roomDataToDomainMapper: RoomDataToDomainMapper<RoomDomain>,
        newRoomDomainToDataMapper: NewRoomDomainToDataMapper<NewRoomDtoData>,
        newRoomDataToRequestMapper: NewRoomDataToRequestMapper<NewRoomRequest>,
        messageRemoteToDataMapper: MessageRemoteToDataMapper<MessageData>,
        messageDataToDomainMapper: MessageDataToDomainMapper<MessageDomain>,
        typingMessageDomainToDataMapper: TypingMessageDtoDomainToDataMapper<TypingMessageDtoData>,
        typingMessageDataToResponseMapper: TypingMessageDataToResponseMapper<TypingMessageDtoResponse>,
        groupRemoteToDataMapper: GroupRemoteToDataMapper<GroupData>,
        groupDataToDomainMapper: GroupDataToDomainMapper<GroupDomain>,
        groupMessageRemoteToDataMapper: GroupMessageRemoteToDataMapper<GroupMessageData>,
        groupMessageDataToDomainMapper: GroupMessageDataToDomainMapper<GroupMessageDomain>,
        groupTypingStatusDomainToDataMapper: GroupTypingStatusDomainToDataMapper<GroupTypingStatusData>,
        groupTypingStatusDataToRequestMapper: GroupTypingStatusDataToRequestMapper<TypingStatusInGroupRequest>,
        driverFormDomainToDataMapper: DriverFormDomainToDataMapper<DriverFormData>,
        driverFormDataToRemoteMapper: DriverFormDataToRemoteMapper<DriverFormRemote>,
        driverFormRemoteToDataMapper: DriverFormRemoteToDataMapper<DriverFormData>,
        driverFormDataToDomainMapper: DriverFormDataToDomainMapper<DriverFormDomain>,
        companionFormDomainToDataMapper: CompanionFormDomainToDataMapper<CompanionFormData>,
        companionFormDataToRemoteMapper: CompanionFormDataToRemoteMapper<CompanionFormRemote>,
        companionFormRemoteToDataMapper: CompanionFormRemoteToDataMapper<CompanionFormData>,
        companionFormDataToDomainMapper: CompanionFormDataToDomainMapper<CompanionFormDomain>,
        companionFormDetailsRemoteToDataMapper: CompanionTripFormDetailsRemoteToDataMapper<CompanionFormDetailsData>,
        driverFormDetailsRemoteToDataMapper: DriverTripFormDetailsRemoteToDataMapper<DriverFormDetailsData>,
        tripFormDataToDomainMapper: TripFormDataToDomainMapper<TripFormDomain>,
        tripNotificationDomainToDataMapper: TripNotificationDomainToDataMapper<TripNotificationData>,
        tripNotificationDataToRemoteMapper: TripNotificationDataToRemoteMapper<TripNotificationRemote>,
        tripNotificationRemoteToDataMapper: TripNotificationRemoteToDataMapper<TripNotificationData>,
        tripNotificationDataToDomainMapper: TripNotificationDataToDomainMapper<TripNotificationDomain>
    ) : Repository {
        return BaseRepository(
            service,
            userResponseToDataMapper,
            userDataToDomainMapper,
            roomResponseToDataMapper,
            roomDataToDomainMapper,
            newRoomDomainToDataMapper,
            newRoomDataToRequestMapper,
            messageRemoteToDataMapper,
            messageDataToDomainMapper,
            typingMessageDomainToDataMapper,
            typingMessageDataToResponseMapper,
            groupRemoteToDataMapper,
            groupDataToDomainMapper,
            groupMessageRemoteToDataMapper,
            groupMessageDataToDomainMapper,
            groupTypingStatusDomainToDataMapper,
            groupTypingStatusDataToRequestMapper,
            driverFormDomainToDataMapper,
            driverFormDataToRemoteMapper,
            driverFormRemoteToDataMapper,
            driverFormDataToDomainMapper,
            companionFormDomainToDataMapper,
            companionFormDataToRemoteMapper,
            companionFormRemoteToDataMapper,
            companionFormDataToDomainMapper,
            companionFormDetailsRemoteToDataMapper,
            driverFormDetailsRemoteToDataMapper,
            tripFormDataToDomainMapper,
            tripNotificationDomainToDataMapper,
            tripNotificationDataToRemoteMapper,
            tripNotificationRemoteToDataMapper,
            tripNotificationDataToDomainMapper
        )
    }

    @Provides
    @Singleton
    fun provideInteractor(
        repository: Repository,
        socketRepository: SocketsRepository,
        localDatabaseRepository: DatabaseRepository,
    ) : Interactor {
        return Interactor.Base(
            repository,
            socketRepository,
            localDatabaseRepository
        )
    }

    @Provides
    @Singleton
    fun provideSocketRepository(
        socketHttpClient: HttpClient,
        roomResponseToDataMapper: RoomResponseToDataMapper<RoomData>,
        roomDataToDomainMapper: RoomDataToDomainMapper<RoomDomain>,
        newRoomDomainToDataMapper: NewRoomDomainToDataMapper<NewRoomDtoData>,
        newRoomDataToRequestMapper: NewRoomDataToRequestMapper<NewRoomRequest>,
        messageDomainToDataMapper: MessageDomainToDataMapper<MessageData>,
        messageDataToRemoteMapper: MessageDataToRemoteMapper<MessageRemote>,
        messageRemoteToDataMapper: MessageRemoteToDataMapper<MessageData>,
        messageDataToDomainMapper: MessageDataToDomainMapper<MessageDomain>,
        lastMsgResponseToDataMapper: NewLastMsgResponseToDataMapper<NewLastMessageInRoomData>,
        lastMsgDataToDomainMapper: NewLastMsgDataToDomainMapper<NewLastMessageInRoomDomain>,
        socketActionsParser: SocketActionsParser,
        groupMessageRemoteToDataMapper: GroupMessageRemoteToDataMapper<GroupMessageData>,
        groupMessageDataToDomainMapper: GroupMessageDataToDomainMapper<GroupMessageDomain>,
        groupMessageDomainToDataMapper: GroupMessageDomainToDataMapper<GroupMessageData>,
        groupMessageDataToRemoteMapper: GroupMessageDataToRemoteMapper<GroupMessageRemote>,
        tripFormRemoteToDataMapper: TripFormRemoteToDataMapper<TripFormData>,
        tripFormDataToDomainMapper: TripFormDataToDomainMapper<TripFormDomain>,
    ) : SocketsRepository {
        return BaseSocketRepository(
            socketHttpClient,
            roomResponseToDataMapper,
            roomDataToDomainMapper,
            newRoomDomainToDataMapper,
            newRoomDataToRequestMapper,
            messageDomainToDataMapper,
            messageDataToRemoteMapper,
            messageRemoteToDataMapper,
            messageDataToDomainMapper,
            lastMsgResponseToDataMapper,
            lastMsgDataToDomainMapper,
            socketActionsParser,
            groupMessageRemoteToDataMapper,
            groupMessageDataToDomainMapper,
            groupMessageDomainToDataMapper,
            groupMessageDataToRemoteMapper,
            tripFormRemoteToDataMapper,
            tripFormDataToDomainMapper
        )
    }

    @Provides
    @Singleton
    fun provideDatabaseRepository(
        roomsDao: RoomsDao,
        groupsDao: GroupsDao,
        notificationsDao: NotificationsDao,
        companionGroupUsersDao: CompanionGroupUsersDao,
        newRoomDataToDbMapper: NewRoomDataToDbMapper<RoomDb>,
        newRoomDomainToDataMapper: NewRoomDomainToDataMapper<NewRoomDtoData>,
        roomDbToDataMapper: RoomDbToDataMapper<RoomData>,
        roomDataToDomainMapper: RoomDataToDomainMapper<RoomDomain>,
        groupMessagesCounterDbToDataMapper: GroupMessagesCounterDbToDataMapper<GroupMessagesCounterData>,
        groupMessagesCounterDataToDomainMapper: GroupMessagesCounterDataToDomainMapper<GroupMessagesCounterDomain>,
        notificationsDbToDataMapper: NotificationDbToDataMapper<TripNotificationData>,
        notificationDataToDomainMapper: TripNotificationDataToDomainMapper<TripNotificationDomain>,
        notificationDomainToDataMapper: TripNotificationDomainToDataMapper<TripNotificationData>,
        notificationDataToDbMapper: TripNotificationDataToDbMapper<NotificationsDb>
    ) : DatabaseRepository {
        return BaseDatabaseRepository(
            roomsDao,
            groupsDao,
            notificationsDao,
            companionGroupUsersDao,
            newRoomDataToDbMapper,
            newRoomDomainToDataMapper,
            roomDbToDataMapper,
            roomDataToDomainMapper,
            groupMessagesCounterDbToDataMapper,
            groupMessagesCounterDataToDomainMapper,
            notificationsDbToDataMapper,
            notificationDataToDomainMapper,
            notificationDomainToDataMapper,
            notificationDataToDbMapper
        )
    }

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        AppDataBase::class.java,
        "database"
    )
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideRoomsDao(db: AppDataBase) = db.roomsDao()

    @Singleton
    @Provides
    fun provideGroupsMessagesCounterDao(db: AppDataBase) = db.groupsDao()

    @Provides
    @Singleton
    fun provideTripNotificationsDao(db: AppDataBase) = db.tripNotificationsDao()

    @Provides
    @Singleton
    fun provideCompanionGroupUsersDao(db: AppDataBase) = db.companionGroupUsersDao()

    @Singleton
    @Provides
    fun provideRoomObserveController(
        newLastMsgInRoomDomainToUiMapper: NewLastMessageInRoomDomainToUiMapper<NewLastMessageInRoomUi>,
        groupLastMessageDomainToUiMapper: GroupLastMessageDomainToUiMapper<GroupLastMessageUi>,
        groupDomainToUiMapper: GroupDomainToUiMapper<GroupUi>
    ) : RoomsObservingSocketController {
        return RoomsObservingSocketController.Base(
            newLastMsgInRoomDomainToUiMapper,
            groupLastMessageDomainToUiMapper,
            groupDomainToUiMapper
        )
    }
}