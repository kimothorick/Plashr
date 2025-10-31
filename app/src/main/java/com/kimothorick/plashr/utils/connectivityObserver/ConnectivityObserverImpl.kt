package com.kimothorick.plashr.utils.connectivityObserver

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Implementation of [ConnectivityObserver] that monitors network connectivity status.
 *
 * This class uses Android's [ConnectivityManager] and [ConnectivityManager.NetworkCallback] to provide a
 * reactive stream of network connection states. It checks for validated network capabilities
 * to determine if the device has an actual internet connection.
 *
 * The provided [isConnected] flow emits `true` when a validated network is available and `false`
 * when it's lost or unavailable. The implementation ensures that the network callback is
 * registered when the flow is collected and unregistered when the collector's coroutine is cancelled,
 * preventing resource leaks.
 *
 * @param context The application context, used to get the [ConnectivityManager] system service.
 */
@Singleton
class ConnectivityObserverImpl
    @Inject
    constructor(
        @ApplicationContext context: Context,
    ) : ConnectivityObserver {
        private val connectivityManager = context.getSystemService<ConnectivityManager>()!!

        override val isConnected: Flow<Boolean>
            get() =
                callbackFlow {
                    val callback =
                        object : NetworkCallback() {
                            override fun onCapabilitiesChanged(
                                network: Network,
                                networkCapabilities: NetworkCapabilities,
                            ) {
                                super.onCapabilitiesChanged(network, networkCapabilities)
                                val connected =
                                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                                trySend(connected)
                            }

                            override fun onAvailable(
                                network: Network,
                            ) {
                                super.onAvailable(network)
                                trySend(true)
                            }

                            override fun onLost(
                                network: Network,
                            ) {
                                super.onLost(network)
                                trySend(false)
                            }

                            override fun onUnavailable() {
                                super.onUnavailable()
                                trySend(false)
                            }
                        }
                    connectivityManager.registerDefaultNetworkCallback(callback)
                    awaitClose {
                        connectivityManager.unregisterNetworkCallback(callback)
                    }
                }
    }
