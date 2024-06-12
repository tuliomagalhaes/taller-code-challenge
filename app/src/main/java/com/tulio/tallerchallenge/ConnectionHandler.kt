package com.tulio.tallerchallenge

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConnectionHandler(
    private val connectivityManager: ConnectivityManager,
) {

    private val networkCapabilities: NetworkCapabilities? =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

    private var networkStatusJob: Job? = null

    val connectionInfo = MutableStateFlow(
        ConnectionInformation(
            status = ConnectionStatus.Disconnected,
            downloadSpeed = 0,
            uploadSpeed = 0,
        )
    )

    fun startListening(coroutineScope: CoroutineScope) {
        // TODO: Inject the Dispatchers instead
        networkStatusJob = coroutineScope.launch(Dispatchers.IO) {
            while(true) {
                delay(100)
                updateNetworkSpeed()
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    fun stopListening() {
        networkStatusJob?.cancel()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun updateNetworkSpeed() {
        if (networkCapabilities == null) return

        // TODO: Check why its not getting the real connection rate
        val downSpeed = networkCapabilities.linkDownstreamBandwidthKbps
        val upSpeed = networkCapabilities.linkUpstreamBandwidthKbps
        connectionInfo.update { it.copy(downloadSpeed = downSpeed, uploadSpeed = upSpeed) }
    }

    private val networkCallback = object: ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            connectionInfo.update { it.copy(status = ConnectionStatus.Connected) }
        }

        override fun onLost(network: Network) {
            connectionInfo.update { it.copy(status = ConnectionStatus.Disconnected) }
        }

        override fun onUnavailable() {
            connectionInfo.update { it.copy(status = ConnectionStatus.Unavailable) }
        }

        override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
            if (blocked) {
                connectionInfo.update { it.copy(status = ConnectionStatus.Blocked) }
            }
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            connectionInfo.update { it.copy(status = ConnectionStatus.PoorConnection) }
        }
    }
}