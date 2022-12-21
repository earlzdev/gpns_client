package com.earl.gpns.di

import android.content.Context
import androidx.room.Room
import com.earl.gpns.data.BaseDatabaseRepository
import com.earl.gpns.data.BaseRepository
import com.earl.gpns.data.BaseSocketRepository
import com.earl.gpns.data.SocketActionsParser
import com.earl.gpns.data.localDb.AppDataBase
import com.earl.gpns.data.localDb.GroupsDao
import com.earl.gpns.data.localDb.RoomDb
import com.earl.gpns.data.localDb.RoomsDao
import com.earl.gpns.data.mappers.*
import com.earl.gpns.data.models.*
import com.earl.gpns.data.models.remote.GroupMessageRemote
import com.earl.gpns.data.models.remote.MessageRemote
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
        groupTypingStatusDataToRequestMapper: GroupTypingStatusDataToRequestMapper<TypingStatusInGroupRequest>
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
            groupTypingStatusDataToRequestMapper
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
        groupMessageDataToRemoteMapper: GroupMessageDataToRemoteMapper<GroupMessageRemote>
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
            groupMessageDataToRemoteMapper
        )
    }

    @Provides
    @Singleton
    fun provideDatabaseRepository(
        roomsDao: RoomsDao,
        groupsDao: GroupsDao,
        newRoomDataToDbMapper: NewRoomDataToDbMapper<RoomDb>,
        newRoomDomainToDataMapper: NewRoomDomainToDataMapper<NewRoomDtoData>,
        roomDbToDataMapper: RoomDbToDataMapper<RoomData>,
        roomDataToDomainMapper: RoomDataToDomainMapper<RoomDomain>,
        groupMessagesCounterDbToDataMapper: GroupMessagesCounterDbToDataMapper<GroupMessagesCounterData>,
        groupMessagesCounterDataToDomainMapper: GroupMessagesCounterDataToDomainMapper<GroupMessagesCounterDomain>
    ) : DatabaseRepository {
        return BaseDatabaseRepository(
            roomsDao,
            groupsDao,
            newRoomDataToDbMapper,
            newRoomDomainToDataMapper,
            roomDbToDataMapper,
            roomDataToDomainMapper,
            groupMessagesCounterDbToDataMapper,
            groupMessagesCounterDataToDomainMapper
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

    @Singleton
    @Provides
    fun provideRoomObserveController(
        newLastMsgInRoomDomainToUiMapper: NewLastMessageInRoomDomainToUiMapper<NewLastMessageInRoomUi>,
        groupLastMessageDomainToUiMapper: GroupLastMessageDomainToUiMapper<GroupLastMessageUi>
    ) : RoomsObservingSocketController {
        return RoomsObservingSocketController.Base(
            newLastMsgInRoomDomainToUiMapper,
            groupLastMessageDomainToUiMapper
        )
    }
}