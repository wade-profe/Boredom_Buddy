package com.example.android.boredombuddy.data.network

import com.example.android.boredombuddy.data.local.DatabaseSuggestion
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

const val BASE_URL_BORED = "https://www.boredapi.com/api/activity/"

data class NetworkSuggestion(
    val activity: String,
    val type: String,
    val participants: Int,
    val price: Double,
    val link: String,
    val key: String,
    val accessibility: Double
)

fun NetworkSuggestion.toDatabaseModel(): DatabaseSuggestion{
   return DatabaseSuggestion(
       id = this.key.toLong(),
       activity = this.activity,
       type = this.type,
       link = this.link,
       imageUrl = null
   )
}

interface SuggestionRequest {

    @GET(".")
    suspend fun getSuggestion(): Response<NetworkSuggestion>

}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object BoredAPI{
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_BORED)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val callApi = retrofit.create(SuggestionRequest::class.java)
}

