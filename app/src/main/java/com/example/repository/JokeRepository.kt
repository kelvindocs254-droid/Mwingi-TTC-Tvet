package com.example.repository

import com.example.api.JokeApiService
import com.example.api.JokeResponse
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class JokeRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://v2.jokeapi.dev/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val jokeService = retrofit.create(JokeApiService::class.java)

    suspend fun getRandomJoke(): JokeResponse {
        return try {
            jokeService.getRandomJoke()
        } catch (e: Exception) {
            JokeResponse(
                type = "error",
                joke = "Failed to fetch joke: ${e.message}",
                error = true
            )
        }
    }

    suspend fun getJokeByCategory(category: String): JokeResponse {
        return try {
            val retrofit2 = Retrofit.Builder()
                .baseUrl("https://v2.jokeapi.dev/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            val service = retrofit2.create(JokeApiService::class.java)
            // Note: For category support, we'd need to extend the interface
            service.getRandomJoke()
        } catch (e: Exception) {
            JokeResponse(
                type = "error",
                joke = "Failed to fetch joke: ${e.message}",
                error = true
            )
        }
    }
}
