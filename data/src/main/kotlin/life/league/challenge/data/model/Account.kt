package life.league.challenge.data.model

import com.google.gson.annotations.SerializedName

data class Account(@SerializedName("api_key") val apiKey: String? = null)
