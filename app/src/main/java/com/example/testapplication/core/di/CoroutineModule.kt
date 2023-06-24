package com.example.testapplication.core.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

enum class WeatherCoroutineDispatcher {
    IO,
    Main,
    MainImmediate,
    Default
}

enum class WeatherCoroutineScopes {
    ApplicationScope
}



val coroutineModule = module {
    single(named(WeatherCoroutineDispatcher.IO)) {
        Dispatchers.IO
    }

    single(named(WeatherCoroutineDispatcher.Main)) {
        Dispatchers.Main
    }

    single(named(WeatherCoroutineDispatcher.Default)) {
        Dispatchers.Default
    }

    single(named(WeatherCoroutineDispatcher.MainImmediate)) {
        Dispatchers.Main.immediate
    }

    single(named(WeatherCoroutineScopes.ApplicationScope)) {
        CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(named(WeatherCoroutineDispatcher.Default)))
    }
}