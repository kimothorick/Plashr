package com.kimothorick.plashr.utils.connectivityObserver

import kotlinx.coroutines.flow.Flow

/**
 * An interface for observing network connectivity status.
 *
 * Implementations of this interface are responsible for monitoring the device's
 * network connection and providing its status as a reactive stream.
 */
interface ConnectivityObserver {
    val isConnected: Flow<Boolean>
}
