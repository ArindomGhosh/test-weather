package com.example.testapplication.data.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal fun GetRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .client(
        OkHttpClient().newBuilder()
            .addInterceptor { chain ->
                val request = chain.request()
                val url = request.url().newBuilder()
                    .addQueryParameter("key", "520916eb3f46442ca1c12926221402").build()
                chain.proceed(request.newBuilder().url(url).build())
            }
            .build()
    )
    .build()


