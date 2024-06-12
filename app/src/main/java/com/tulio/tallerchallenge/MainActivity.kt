package com.tulio.tallerchallenge

import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tulio.tallerchallenge.ui.theme.TallerChallengeTheme


class MainActivity : ComponentActivity() {

    private val viewModel: ConnectionStatusViewModel by viewModels {
        val connectivityManager = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val connectionHandler = ConnectionHandler(connectivityManager)
        ConnectionStatusViewModel.provideFactory(connectionHandler, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsState(initial = ConnectionStatusUiState())
            TallerChallengeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        // TODO: Get strings from resources
                        Text("Network status: ${uiState.state}")
                        Text("Network download speed: ${uiState.downloadSpeed}")
                        Text("Network upload speed: ${uiState.uploadSpeed}")
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startListening()
    }

    override fun onPause() {
        super.onPause()
//        viewModel.stopListening()
    }
}