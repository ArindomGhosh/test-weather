package com.example.testapplication.core.di

import com.example.testapplication.core.network.ConnectivityManagerNetworkMonitor
import com.example.testapplication.core.network.NetworkMonitor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val coreModule = module {
    includes(coroutineModule)
    single<NetworkMonitor> {
        ConnectivityManagerNetworkMonitor(androidApplication())
    }
}