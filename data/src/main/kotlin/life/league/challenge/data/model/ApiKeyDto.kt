package life.league.challenge.data.model

import com.google.gson.annotations.SerializedName
import life.league.challenge.domain.model.APIKey

/**
 * DTO returned by the login endpoint. Keep this class internal to the data layer and expose the
 * validated API key through [toDomainApiKey] when handing the response across layers.
 */
data class ApiKeyDto(@SerializedName("api_key") val apiKey: String? = null)

internal fun ApiKeyDto.toDomainApiKey(): APIKey = APIKey(requireApiKey())

internal fun ApiKeyDto.requireApiKey(): String =
    apiKey?.takeIf(String::isNotBlank) ?: throw IllegalStateException("Login response missing an API key")
