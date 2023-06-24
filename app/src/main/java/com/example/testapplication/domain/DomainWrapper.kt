package com.example.testapplication.domain

sealed interface DomainWrapper<T:Any>{
    data class Success<T:Any>(val entity:T):DomainWrapper<T>
    data class Failure(val errorEntity: ErrorEntity):DomainWrapper<Nothing>
}
