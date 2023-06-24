package com.example.testapplication.domain

import com.example.testapplication.data.repo.ApiResponse
import com.example.testapplication.data.repo.WeatherRepo
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetWeatherInfoInParalleUseCase(
    private val weatherRepo: WeatherRepo
) {
    suspend operator fun invoke(places: List<String>): Flow<DomainWrapper<out List<WeatherEntity>>> =
        coroutineScope {
            val reqs = places.map {
                async { weatherRepo.getWeatherinfo(it) }
            }
            val weatherEntities = mutableListOf<WeatherEntity>()
            val uiErrorEntites = mutableListOf<ErrorEntity>()
            combine(reqs.awaitAll().toList()) { apiResponses ->
                for (apires in apiResponses) {
                    if (apires is ApiResponse.Failure) {
                        uiErrorEntites.add(
                            ErrorEntity(
                                apires.throwable.message ?: "",
                                apires.throwable
                            )
                        )
                        break
                    } else {
                        weatherEntities.add(
                            (apires as ApiResponse.Success).response.toWeatherEntity()
                        )
                    }
                }
                if (uiErrorEntites.isNotEmpty()) {
                    DomainWrapper.Failure(errorEntity = uiErrorEntites.first())
                } else {
                    DomainWrapper.Success(entity = weatherEntities.toList())
                }
            }
        }
}