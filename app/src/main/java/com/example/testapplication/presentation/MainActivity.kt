package com.example.testapplication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.testapplication.ui.theme.TestApplicationTheme
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity() {
    private val _weatherViewModel: WeatherViewmodel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestApplicationTheme {
                // A surface container using the 'background' color from the theme
                val snackbarHostState = remember { SnackbarHostState() }
                LaunchedEffect(Unit) {
//                    Un-comment either of the code to test the respective use-cases

//                    _weatherViewModel.getWeatherInSequence(
//                        listOf(
//                            "Kolkata",
//                            "visakhapatnam",
//                            "Mumbai"
//                        )
//                    )
                    _weatherViewModel.getWeatherInParallel(
                        listOf(
                            "Kolkata",
                            "visakhapatnam",
                            "Mumbai"
                        )
                    )
                }
                val uiState by _weatherViewModel.uiStateFlow.collectAsStateWithLifecycle()
                WeatherListScreen(
                    uiState = uiState,
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeatherListScreen(
    uiState: WeatherUIState,
    snackbarHostState: SnackbarHostState
) {
    if (!uiState.isOnline) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = "Not connected to internet"
            )
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            if (uiState.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )
            } else if (uiState.isError) {
                Text(text = "Error")
            } else {
                Column {
                    Text(text = "${uiState.count}")
                    LazyColumn {
                        items(uiState.weatherList) {
                            Row {
                                Text(text = "${it.name}:${it.country}: ${it.tempC}")
                            }
                        }
                    }
                }
            }
        }
    }
}