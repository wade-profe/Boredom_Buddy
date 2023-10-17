package com.example.android.boredombuddy.data.network

import com.example.android.boredombuddy.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


const val BASE_URL_PEXEL = "https://api.pexels.com/v1/"

data class TotalResult(
    @Json(name = "total_results") val totalResults: Int,
    val page: Int,
    @Json(name = "per_page") val perPage: Int,
    val photos: List<PhotoResult>,
    @Json(name = "next_page") val nextPage: String)

data class PhotoResult(
    val id: Long,
    val width: Int,
    val height: Int,
    val url: String,
    val photographer: String,
    @Json(name = "photographer_url") val photographerUrl: String,
    @Json(name = "photographer_id") val photographerId: Long,
    @Json(name = "avg_color") val avgColor: String,
    val src: Src,
    val liked: Boolean,
    val alt: String)

data class Src(
    val original: String,
    val large2x: String,
    val large: String,
    val medium: String,
    val small: String,
    val portrait: String,
    val landscape: String,
    val tiny: String)

fun TotalResult.provideImageUrl(): String{
    return photos[0].src.landscape
}

interface ImageSearch{
    @GET("search?per_page=1")
    suspend fun getImage(@Header("Authorization") header: String = BuildConfig.PEXEL_API_KEY,
                         @Query("query") query: String): Response<TotalResult>
}


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object PexelAPI {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_PEXEL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val callApi = retrofit.create(ImageSearch::class.java)
}



