package com.tulio.tallerchallenge

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ConnectionStatusViewModel(
    private val connectionHandler: ConnectionHandler,
) : ViewModel() {

    val uiState = connectionHandler.connectionInfo.map { it.toUiState() }

    fun startListening() {
        viewModelScope.launch{
            connectionHandler.startListening(this)
        }
    }

    fun stopListening() {
        connectionHandler.stopListening()
    }

    private fun ConnectionInformation.toUiState() = ConnectionStatusUiState(
        downloadSpeed = downloadSpeed.toString(),
        uploadSpeed = uploadSpeed.toString(),
        state = status.toString(), // TODO: Get strings from resources
    )

    companion object {
        fun provideFactory(
            connectionHandler: ConnectionHandler,
            owner: SavedStateRegistryOwner,
        ): AbstractSavedStateViewModelFactory =
            object : AbstractSavedStateViewModelFactory(owner, null) {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return ConnectionStatusViewModel(connectionHandler) as T
                }
            }
    }
}