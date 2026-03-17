package life.league.challenge.data.repository

import kotlinx.coroutines.delay
import life.league.challenge.domain.model.APIKey
import life.league.challenge.domain.repository.AccountRepository
import life.league.challenge.domain.usecase.login.LoginResult
import javax.inject.Inject

/**
 * Test double for [life.league.challenge.domain.repository.AccountRepository]. Configure [result] and optionally [delayMs]
 * for unit and instrumented tests.
 */
class MockAccountRepositoryImpl @Inject constructor() : AccountRepository {

    val result: LoginResult = LoginResult.Success(APIKey("test-key"))

    var delayMs: Long = 50L

    override suspend fun signIn(username: String, password: CharArray): LoginResult {
        if (delayMs > 0) delay(delayMs)
        return result
    }
}