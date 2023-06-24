package com.example.testapplication.data.services

import com.example.testapplication.data.dto.WeatherDto
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Query


interface WeatherApi {
    @GET("current.json")
    suspend fun getWeatherInfo(
        @Query("q") place: String
    ): Response<WeatherDto>
}