package com.example.testapplication.domain

import com.example.testapplication.data.repo.ApiResponse
import com.example.testapplication.data.repo.WeatherRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetWeatherInfoInSequenceUseCase(
    private val weatherRepo: WeatherRepo,
    private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(places: List<String>): Flow<DomainWrapper<out WeatherEntity>> {
        return places.asFlow()
            .flatMapMerge {
                // Introducing delay to show sequential update
                delay(2000)
                weatherRepo.getWeatherinfo(it)
            }.map { apiRes ->
                when (apiRes) {
                    is ApiResponse.Failure -> DomainWrapper.Failure(
                        ErrorEntity(
                            apiRes.throwable.message ?: "",
                            apiRes.throwable
                        )
                    )

                    is ApiResponse.Success -> DomainWrapper.Success(
                        apiRes.response.toWeatherEntity()
                    )
                }
            }.flowOn(defaultDispatcher)
    }
}