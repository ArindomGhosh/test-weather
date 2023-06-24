package com.example.testapplication.data.repo

import com.example.testapplication.data.dto.WeatherDto
import com.example.testapplication.data.services.WeatherApi
import com.example.testapplication.data.services.WeatherService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

sealed interface ApiResponse<T : Any> {
    data class Success<T : Any>(val response: T) : ApiResponse<T>
    data class Failure(val throwable: Throwable) : ApiResponse<Nothing>
}


interface WeatherRepo {
    suspend fun getWeatherinfo(placeName: String): Flow<ApiResponse<out WeatherDto>>
}

class WeatherRepoImpl(
    private val weatherService: WeatherService,
    private val ioDispatcher: CoroutineDispatcher,
    private val coroutineScope: CoroutineScope
) : WeatherRepo {
    private val weatherApi: WeatherApi by lazy { weatherService.getWeatherApi() }

    override suspend fun getWeatherinfo(placeName: String): Flow<ApiResponse<out WeatherDto>> {
        return flow {
            try {
                val response = weatherApi.getWeatherInfo(place = placeName)
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        emit(ApiResponse.Success(response = response.body()!!))
                    } else {
                        emit(ApiResponse.Failure(Throwable("Empty response")))
                    }
                } else {
                    emit(ApiResponse.Failure(Throwable("Unknown issue")))
                }
            }catch (exc: Exception){
                emit(ApiResponse.Failure(exc))
            }
        }.flowOn(ioDispatcher)
    }
}

