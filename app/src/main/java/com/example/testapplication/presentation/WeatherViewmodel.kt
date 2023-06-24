package com.example.testapplication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapplication.domain.DomainWrapper
import com.example.testapplication.domain.GetWeatherInfoInParalleUseCase
import com.example.testapplication.domain.GetWeatherInfoInSequenceUseCase
import com.example.testapplication.domain.WeatherEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class WeatherUIState(
//    adding counter to show how often ui Updated for sequence and parallel
    val count: Int = 0,
    val isLoading: Boolean = false,
    val weatherList: List<WeatherEntity> = emptyList(),
    val isError: Boolean = false
)


class WeatherViewmodel(
    private val getWeatherInfoSequenceUseCase: GetWeatherInfoInSequenceUseCase,
    private val getWeatherInfoInParalleUseCase: GetWeatherInfoInParalleUseCase
) : ViewModel() {
    private val _uiStateFlow: MutableStateFlow<WeatherUIState> = MutableStateFlow<WeatherUIState>(
        WeatherUIState()
    )
    val uiStateFlow: StateFlow<WeatherUIState> = _uiStateFlow.asStateFlow()


    fun getWeatherInSequence(places: List<String>) {
        viewModelScope.launch {
            getWeatherInfoSequenceUseCase.invoke(places)
                .onStart {
                    _uiStateFlow.update {
                        it.copy(isLoading = true)
                    }
                }
                .onEach { domainWrapper ->
                    when (domainWrapper) {
                        is DomainWrapper.Failure -> _uiStateFlow.update {
                            it.copy(isError = true, isLoading = false)
                        }

                        is DomainWrapper.Success -> _uiStateFlow.update {
                            it.copy(
                                weatherList = it.weatherList + domainWrapper.entity,
                                isLoading = false,
                                count = it.count + 1
                            )
                        }
                    }

                }
                .catch {
                    _uiStateFlow.update {
                        it.copy(isError = true, isLoading = false)
                    }
                }.collect()
        }
    }


    fun getWeatherInParallel(places:List<String>) {
        viewModelScope.launch {
            getWeatherInfoInParalleUseCase.invoke(places)
                .onStart {
                    _uiStateFlow.update {
                        it.copy(isLoading = true)
                    }
                }
                .onEach { domainWrapper ->
                    when (domainWrapper) {
                        is DomainWrapper.Failure -> _uiStateFlow.update {
                            it.copy(isError = true, isLoading = false)
                        }

                        is DomainWrapper.Success -> _uiStateFlow.update {
                            it.copy(weatherList = domainWrapper.entity, isLoading = false, count = it.count+1)
                        }
                    }
                }
                .catch {
                    _uiStateFlow.update {
                        it.copy(isError = true, isLoading = false)
                    }
                }.collect()
        }
    }

}