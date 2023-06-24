package com.example.testapplication.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest.Builder
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.content.getSystemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class ConnectivityManagerNetworkMonitor constructor(
    private val context: Context,
) : NetworkMonitor {
    override val isOnline: Flow<Boolean> = callbackFlow {
        val connectivityManager = context.getSystemService<ConnectivityManager>()

        /**
         * The callback's methods are invoked on changes to *any* network, not just the active
         * network. So to check for network connectivity, one must query the active network of the
         * ConnectivityManager.
         */
        val callback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                channel.trySend(connectivityManager.isCurrentlyConnected())
            }

            override fun onLost(network: Network) {
                channel.trySend(connectivityManager.isCurrentlyConnected())
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities,
            ) {
                channel.trySend(connectivityManager.isCurrentlyConnected())
            }
        }

        connectivityManager?.registerNetworkCallback(
            Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build(),
            callback,
        )

        channel.trySend(connectivityManager.isCurrentlyConnected())

        awaitClose {
            connectivityManager?.unregisterNetworkCallback(callback)
        }
    }
        .conflate()

    @Suppress("DEPRECATION")
    private fun ConnectivityManager?.isCurrentlyConnected() = when (this) {
        null -> false
        else -> when {
            VERSION.SDK_INT >= VERSION_CODES.M ->
                activeNetwork
                    ?.let(::getNetworkCapabilities)
                    ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    ?: false

            else -> activeNetworkInfo?.isConnected ?: false
        }
    }
}

fun NetworkMonitor.asSharedFlow(
    coroutineScope: CoroutineScope
): SharedFlow<Boolean> = this.isOnline.distinctUntilChanged().shareIn(
    scope = coroutineScope,
    started = SharingStarted.WhileSubscribed(5_000),
)

fun NetworkMonitor.asStateFlow(
    coroutineScope: CoroutineScope
): SharedFlow<Boolean> = this.isOnline.distinctUntilChanged().stateIn(
    scope = coroutineScope,
    started = SharingStarted.WhileSubscribed(5_000),
    initialValue = true
)

