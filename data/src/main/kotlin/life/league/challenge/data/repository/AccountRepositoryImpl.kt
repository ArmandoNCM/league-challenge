package life.league.challenge.data.repository

import android.util.Log
import life.league.challenge.data.api.AccountApi
import life.league.challenge.data.model.toDomainApiKey
import life.league.challenge.domain.usecase.login.LoginError
import life.league.challenge.domain.usecase.login.LoginResult
import life.league.challenge.domain.repository.AccountRepository
import okhttp3.Credentials
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountApi: AccountApi
) : AccountRepository {

    companion object {
        private const val TAG = "AccountRepositoryImpl"
        /** Upper bound for 5xx server error codes (inclusive). */
        private const val SERVER_ERROR_MAX = 599
    }

    override suspend fun signIn(username: String, password: CharArray): LoginResult {
        Log.d(TAG, "signIn: attempting sign-in for user=$username")
        val credential = Credentials.basic(username, String(password))
        password.fill('\u0000')
        return try {
            val apiKey = accountApi.login(credential).toDomainApiKey()
            Log.d(TAG, "signIn: success for user=$username")
            LoginResult.Success(apiKey)
        } catch (e: HttpException) {
            val error = when (e.code()) {
                HttpURLConnection.HTTP_UNAUTHORIZED -> LoginError.Unauthorized
                in HttpURLConnection.HTTP_INTERNAL_ERROR..SERVER_ERROR_MAX -> LoginError.ServerError
                else -> LoginError.ServerError
            }
            Log.d(TAG, "signIn: HTTP error code=${e.code()}, error=$error")
            LoginResult.Failure(error)
        } catch (e: IOException) {
            Log.d(TAG, "signIn: network error", e)
            LoginResult.Failure(LoginError.NetworkError)
        } catch (e: IllegalStateException) {
            Log.d(TAG, "signIn: invalid response", e)
            LoginResult.Failure(LoginError.InvalidResponse)
        } catch (e: Exception) {
            Log.d(TAG, "signIn: unknown error", e)
            LoginResult.Failure(LoginError.Unknown)
        }
    }
}
