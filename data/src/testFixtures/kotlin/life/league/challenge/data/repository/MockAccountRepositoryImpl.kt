package life.league.challenge.data.repository

import kotlinx.coroutines.delay
import life.league.challenge.domain.repository.AccountRepository
import life.league.challenge.domain.usecase.login.LoginError
import life.league.challenge.domain.usecase.login.LoginResult
import javax.inject.Inject

/**
 * Test double for [life.league.challenge.domain.repository.AccountRepository]. Configure [result] and optionally [delayMs]
 * for unit and instrumented tests.
 */
class MockAccountRepositoryImpl @Inject constructor() : AccountRepository {

    var result: LoginResult = LoginResult.Failure(LoginError.Unauthorized)
        set(value) { field = value }

    var delayMs: Long = 0L
        set(value) { field = value }

    override suspend fun signIn(username: String, password: CharArray): LoginResult {
        if (delayMs > 0) delay(delayMs)
        return result
    }
}