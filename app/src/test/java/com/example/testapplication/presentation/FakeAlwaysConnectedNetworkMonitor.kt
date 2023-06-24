package com.example.testapplication.presentation

import com.example.testapplication.core.network.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeAlwaysConnectedNetworkMonitor : NetworkMonitor {
    override val isOnline: Flow<Boolean>
        get() = flowOf(true)
}