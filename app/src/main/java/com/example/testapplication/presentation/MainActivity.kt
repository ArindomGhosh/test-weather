package com.example.testapplication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                LaunchedEffect(Unit) {
//                    _weatherViewModel.getWeatherInSequence(listOf("Kolkata", "visakhapatnam", "Mumbai"))
                    _weatherViewModel.getWeatherInParallel(listOf("Kolkata", "visakhapatnam", "Mumbai"))
                }

                val uistate by _weatherViewModel.uiStateFlow.collectAsStateWithLifecycle()
                if (uistate.isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                    )
                } else if (uistate.isError) {
                    Text(text = "Error")
                } else {
                    Column() {
                        Text(text = "${uistate.count}")
                        LazyColumn {
                            items(uistate.weatherList) {
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
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestApplicationTheme {
        Greeting("Android")
    }
}