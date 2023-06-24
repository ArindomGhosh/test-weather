package com.example.testapplication.data.dto

data class WeatherDto(
    val location: Location,
    val current: Current
)

data class Current(
    val temp_c: Double,
    val temp_f: Double
)

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
)
