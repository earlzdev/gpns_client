package com.earl.gpns.di

import com.earl.gpns.data.BaseRepository
import com.earl.gpns.data.retrofit.Service
import com.earl.gpns.domain.Interactor
import com.earl.gpns.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRepository(
        service: Service
    ) : Repository {
        return BaseRepository(
            service
        )
    }

    @Provides
    @Singleton
    fun provideInteractor(
        repository: Repository
    ) : Interactor {
        return Interactor.Base(
            repository
        )
    }
}