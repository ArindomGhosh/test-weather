package com.example.testapplication.domain

import com.example.testapplication.data.dto.WeatherDto

data class WeatherEntity(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val tempC:String
)

fun WeatherDto.toWeatherEntity()=WeatherEntity(
    name = this.location.name,
    region = this.location.region,
    country = this.location.country,
    lat=this.location.lat,
    lon = this.location.lon,
    tempC = "${this.current.temp_c} C"
)


