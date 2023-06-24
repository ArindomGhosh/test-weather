package com.example.testapplication.presentation.di

import com.example.testapplication.domain.di.domainModule
import com.example.testapplication.presentation.WeatherViewmodel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    includes(domainModule)
    viewModel { WeatherViewmodel(get(),get(),get()) }
}