package life.league.challenge.data.repository

import life.league.challenge.data.api.AccountApi
import life.league.challenge.data.model.toDomainApiKey
import life.league.challenge.domain.model.APIKey
import life.league.challenge.domain.repository.AccountRepository
import okhttp3.Credentials
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountApi: AccountApi
) : AccountRepository {

    override suspend fun signIn(username: String, password: CharArray): APIKey {
        val credential = Credentials.basic(username, String(password))
        password.fill('\u0000')
        return accountApi.login(credential).toDomainApiKey()
    }
}
