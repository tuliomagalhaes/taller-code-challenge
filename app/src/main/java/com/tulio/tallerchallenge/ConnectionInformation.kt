package com.tulio.tallerchallenge

enum class ConnectionStatus {
    Connected,
    Disconnected,
    Unavailable,
    PoorConnection,
    Blocked,
}

data class ConnectionInformation(
    val status: ConnectionStatus,
    val uploadSpeed: Int,
    val downloadSpeed: Int,
)