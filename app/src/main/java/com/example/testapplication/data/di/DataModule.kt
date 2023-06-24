package com.example.testapplication.data.di

import com.example.testapplication.core.di.WeatherCoroutineDispatcher
import com.example.testapplication.core.di.WeatherCoroutineScopes
import com.example.testapplication.core.di.coreModule
import com.example.testapplication.data.repo.WeatherRepo
import com.example.testapplication.data.repo.WeatherRepoImpl
import com.example.testapplication.data.services.WeatherService
import com.example.testapplication.data.services.WeatherServiceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val datamodule = module {
    includes(coreModule)

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