package com.example.api

import retrofit2.http.GET
import retrofit2.http.Query

// Data models for joke API
data class JokeResponse(
    val type: String,
    val setup: String? = null,
    val delivery: String? = null,
    val joke: String? = null,
    val error: Boolean = false
)

interface JokeApiService {
    @GET("joke/Any")
    suspend fun getRandomJoke(
        @Query("format") format: String = "json",
        @Query("safe-mode") safeMode: Boolean = true
    ): JokeResponse
}
