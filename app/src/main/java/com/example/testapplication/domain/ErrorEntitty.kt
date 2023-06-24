package com.example.testapplication.domain

data class ErrorEntity(
    val message:String,
    val throwable: Throwable
)


