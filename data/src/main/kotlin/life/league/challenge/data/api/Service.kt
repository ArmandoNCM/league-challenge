package life.league.challenge.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Service {

    // TODO Update to use BuildConfig field
    private const val HOST = "localhost"
    private const val TAG = "Service"

    val api: Api by lazy {
        val retrofit = Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        retrofit.create<Api>(Api::class.java)
    }
}
