package com.earl.gpns.di

import android.content.Context
import androidx.room.Room
import com.earl.gpns.data.BaseDatabaseRepository
import com.earl.gpns.data.BaseRepository
import com.earl.gpns.data.BaseSocketRepository
import com.earl.gpns.data.local.AppDataBase
import com.earl.gpns.data.local.RoomDb
import com.earl.gpns.data.local.RoomsDao
import com.earl.gpns.data.mappers.*
import com.earl.gpns.data.models.*
import com.earl.gpns.data.retrofit.Service
import com.earl.gpns.data.models.remote.MessageRemote
import com.earl.gpns.data.models.remote.requests.NewRoomRequest
import com.earl.gpns.domain.Interactor
import com.earl.gpns.domain.mappers.MessageDomainToDataMapper
import com.earl.gpns.domain.mappers.NewRoomDomainToDataMapper
import com.earl.gpns.domain.models.MessageDomain
import com.earl.gpns.domain.models.NewLastMessageInRoomDomain
import com.earl.gpns.domain.models.RoomDomain
import com.earl.gpns.domain.models.UserDomain
import com.earl.gpns.domain.DatabaseRepository
import com.earl.gpns.domain.Repository
import com.earl.gpns.domain.SocketsRepository
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
        messageDataToDomainMapper: MessageDataToDomainMapper<MessageDomain>
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
            messageDataToDomainMapper
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
        lastMsgDataToDomainMapper: NewLastMsgDataToDomainMapper<NewLastMessageInRoomDomain>
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
            lastMsgDataToDomainMapper
        )
    }

    @Provides
    @Singleton
    fun provideDatabaseRepository(
        roomsDao: RoomsDao,
        newRoomDataToDbMapper: NewRoomDataToDbMapper<RoomDb>,
        newRoomDomainToDataMapper: NewRoomDomainToDataMapper<NewRoomDtoData>,
        roomDbToDataMapper: RoomDbToDataMapper<RoomData>,
        roomDataToDomainMapper: RoomDataToDomainMapper<RoomDomain>
    ) : DatabaseRepository {
        return BaseDatabaseRepository(
            roomsDao,
            newRoomDataToDbMapper,
            newRoomDomainToDataMapper,
            roomDbToDataMapper,
            roomDataToDomainMapper
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
}