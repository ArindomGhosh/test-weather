package com.example.testapplication.presentation

import com.example.testapplication.domain.di.domainModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    includes(domainModule)
    viewModel { WeatherViewmodel(get(),get()) }
}