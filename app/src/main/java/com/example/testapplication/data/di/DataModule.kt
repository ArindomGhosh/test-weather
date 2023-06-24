package com.example.testapplication.data.di

import com.example.testapplication.core.WeatherCoroutineDispatcher
import com.example.testapplication.core.WeatherCoroutineScopes
import com.example.testapplication.core.coroutineModule
import com.example.testapplication.data.repo.WeatherRepo
import com.example.testapplication.data.repo.WeatherRepoImpl
import com.example.testapplication.data.services.WeatherService
import com.example.testapplication.data.services.WeatherServiceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val datamodule = module {
    includes(coroutineModule)

    single<WeatherService> {
        WeatherServiceImpl()
    }
    single<WeatherRepo> {
        WeatherRepoImpl(
            get(),
            get(named(WeatherCoroutineDispatcher.IO)),
            get(named(WeatherCoroutineScopes.ApplicationScope))
        )
    }
}