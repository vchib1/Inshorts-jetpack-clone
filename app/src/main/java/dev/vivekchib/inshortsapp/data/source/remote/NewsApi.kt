package dev.vivekchib.inshortsapp.data.source.remote

import dev.vivekchib.inshortsapp.core.constants.API
import dev.vivekchib.inshortsapp.data.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApi {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String = API.SECRET_KEY
    ): Response<NewsResponse>
}