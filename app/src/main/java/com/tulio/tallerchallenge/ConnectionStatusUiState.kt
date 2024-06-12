package com.tulio.tallerchallenge

import androidx.compose.runtime.Immutable

@Immutable
data class ConnectionStatusUiState(
    val state: String = "",
    val downloadSpeed: String = "",
    val uploadSpeed: String = "",
)