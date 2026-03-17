package life.league.challenge.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import life.league.challenge.data.repository.MockAccountRepositoryImpl
import life.league.challenge.domain.repository.AccountRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TestRepositoryModule {

    @Binds
    @Singleton
    abstract fun provideAccountRepository(impl: MockAccountRepositoryImpl): AccountRepository

}