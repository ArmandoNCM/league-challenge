package life.league.challenge.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import life.league.challenge.domain.repository.AccountRepository
import life.league.challenge.domain.usecase.login.LoginUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: AccountRepository): LoginUseCase =
        LoginUseCase(repository)
}
