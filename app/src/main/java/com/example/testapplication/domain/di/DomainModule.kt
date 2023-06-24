package com.example.testapplication.domain.di

import com.example.testapplication.core.di.WeatherCoroutineDispatcher
import com.example.testapplication.data.di.datamodule
import com.example.testapplication.domain.GetWeatherInfoInParalleUseCase
import com.example.testapplication.domain.GetWeatherInfoInSequenceUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val domainModule = module {
    includes(datamodule)
    single {
        GetWeatherInfoInParalleUseCase(get(), get(named(WeatherCoroutineDispatcher.Default)))
    }
    single {
        GetWeatherInfoInSequenceUseCase(get(), get(named(WeatherCoroutineDispatcher.Default)))
    }
}