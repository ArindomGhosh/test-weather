package com.example.testapplication.presentation

import com.example.testapplication.domain.DomainWrapper
import com.example.testapplication.domain.GetWeatherInfoInParalleUseCase
import com.example.testapplication.domain.GetWeatherInfoInSequenceUseCase
import com.example.testapplication.domain.WeatherEntity
import com.example.testapplication.utils.MainCoroutineRule
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WeatherViewmodelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var mockGetWeatherInfoSequenceUC: GetWeatherInfoInSequenceUseCase

    @Mock
    private lateinit var mockGetWeatherinParallUC: GetWeatherInfoInParalleUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `test uiState for inParallel call`() = mainCoroutineRule.testScope.runTest {
        `when`(mockGetWeatherinParallUC.invoke(listOf("Kolkata", "Mumbai", "Pune")))
            .thenReturn(
                flow {
                    emit(
                        DomainWrapper.Success(
                            entity = listOf(
                                WeatherEntity(
                                    "Kolkata",
                                    "West Bengal",
                                    "India",
                                    22.9,
                                    28.9,
                                    "22.0 C"
                                ),
                                WeatherEntity(
                                    "Mumbai",
                                    "Maharastra",
                                    "India",
                                    22.9,
                                    28.9,
                                    "22.0 C"
                                ),
                                WeatherEntity(
                                    "Pune",
                                    "Maharastra",
                                    "India",
                                    22.9,
                                    28.9,
                                    "22.0 C"
                                )
                            )
                        )
                    )
                }
            )
        val sutWeatherViewmodel = WeatherViewmodel(
            getWeatherInfoInParalleUseCase = mockGetWeatherinParallUC,
            getWeatherInfoSequenceUseCase = mockGetWeatherInfoSequenceUC
        )
        sutWeatherViewmodel.getWeatherInParallel(listOf("Kolkata", "Mumbai", "Pune"))
        advanceUntilIdle()
        val uiState = sutWeatherViewmodel.uiStateFlow.value
        Assert.assertEquals(
            WeatherUIState(
                count = 1,
                isLoading = false,
                weatherList = listOf(
                    WeatherEntity(
                        "Kolkata",
                        "West Bengal",
                        "India",
                        22.9,
                        28.9,
                        "22.0 C"
                    ),
                    WeatherEntity(
                        "Mumbai",
                        "Maharastra",
                        "India",
                        22.9,
                        28.9,
                        "22.0 C"
                    ),
                    WeatherEntity(
                        "Pune",
                        "Maharastra",
                        "India",
                        22.9,
                        28.9,
                        "22.0 C"
                    )
                ),
                isError = false
            ), uiState
        )
    }

    @Test
    fun `test uiState for inSequence call`() = mainCoroutineRule.testScope.runTest {
        `when`(mockGetWeatherInfoSequenceUC.invoke(listOf("Kolkata", "Mumbai", "Pune")))
            .thenReturn(
                flow {
                    emit(
                        DomainWrapper.Success(
                            WeatherEntity(
                                "Kolkata",
                                "West Bengal",
                                "India",
                                22.9,
                                28.9,
                                "22.0 C"
                            ),
                        )
                    )
                    emit(
                        DomainWrapper.Success(
                            entity = WeatherEntity(
                                "Mumbai",
                                "Maharastra",
                                "India",
                                22.9,
                                28.9,
                                "22.0 C"
                            ),
                        )
                    )
                    emit(
                        DomainWrapper.Success(
                            entity = WeatherEntity(
                                "Pune",
                                "Maharastra",
                                "India",
                                22.9,
                                28.9,
                                "22.0 C"
                            )
                        )
                    )
                }
            )
        val sutWeatherViewmodel = WeatherViewmodel(
            getWeatherInfoInParalleUseCase = mockGetWeatherinParallUC,
            getWeatherInfoSequenceUseCase = mockGetWeatherInfoSequenceUC
        )
        sutWeatherViewmodel.getWeatherInSequence(listOf("Kolkata", "Mumbai", "Pune"))
        advanceUntilIdle()
        val uiState = sutWeatherViewmodel.uiStateFlow.value
        Assert.assertEquals(
            WeatherUIState(
                count = 3,
                isLoading = false,
                weatherList = listOf(
                    WeatherEntity(
                        "Kolkata",
                        "West Bengal",
                        "India",
                        22.9,
                        28.9,
                        "22.0 C"
                    ),
                    WeatherEntity(
                        "Mumbai",
                        "Maharastra",
                        "India",
                        22.9,
                        28.9,
                        "22.0 C"
                    ),
                    WeatherEntity(
                        "Pune",
                        "Maharastra",
                        "India",
                        22.9,
                        28.9,
                        "22.0 C"
                    )
                ),
                isError = false
            ), uiState
        )
    }
}