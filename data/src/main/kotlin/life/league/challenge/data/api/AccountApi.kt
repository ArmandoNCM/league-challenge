package life.league.challenge.data.api

import life.league.challenge.data.model.ApiKeyDto
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * Retrofit API interface definition using coroutines. Feel free to change this implementation to
 * suit your chosen architecture pattern and concurrency tools
 */
interface AccountApi {

    /**
     * Basic Authorization Log-in service which returns an [ApiKeyDto] object containing an API Key
     * for subsequent REST API calls
     */
    @GET("login")
    suspend fun login(@Header("Authorization") credentials: String?): ApiKeyDto

}