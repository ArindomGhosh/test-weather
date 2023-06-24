package com.example.testapplication.data.services


interface WeatherService {
    fun getWeatherApi(): WeatherApi
}

class WeatherServiceImpl : WeatherService {
    override fun getWeatherApi(): WeatherApi {
        return GetRetrofit("https://api.weatherapi.com/v1/")
            .create(WeatherApi::class.java)
    }
}