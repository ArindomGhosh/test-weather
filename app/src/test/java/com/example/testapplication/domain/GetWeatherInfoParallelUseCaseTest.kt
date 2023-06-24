package com.example.testapplication.domain

import com.example.testapplication.data.dto.Current
import com.example.testapplication.data.dto.Location
import com.example.testapplication.data.dto.WeatherDto
import com.example.testapplication.data.repo.ApiResponse
import com.example.testapplication.data.repo.WeatherRepo
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetWeatherInfoParallelUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var mockWeatherRepo: WeatherRepo

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `test GetWeatherInfoInParalleUseCase return response in sequence`() = runTest(testDispatcher) {
        val mockReqRes = mapOf(
            "Kolkata" to WeatherDto(
                location = Location(
                    name = "Kolkata",
                    region = "West Bengal",
                    country = "India",
                    lat = 22.9,
                    lon = 22.8
                ),
                current = Current(
                    temp_f = 84.0,
                    temp_c = 29.0
                )
            ),
            "Mumbai" to WeatherDto(
                location = Location(
                    name = "Mumbai",
                    region = "Maharastra",
                    country = "India",
                    lat = 22.9,
                    lon = 22.8
                ),
                current = Current(
                    temp_f = 84.0,
                    temp_c = 29.0
                )
            ),
            "Pune" to WeatherDto(
                location = Location(
                    name = "Pune",
                    region = "Maharastra",
                    country = "India",
                    lat = 22.9,
                    lon = 22.8
                ),
                current = Current(
                    temp_f = 84.0,
                    temp_c = 29.0
                )
            )
        )
        mockReqRes.forEach { (key, value) ->
            Mockito.`when`(mockWeatherRepo.getWeatherinfo(key))
                .thenReturn(flowOf(ApiResponse.Success(response = mockReqRes[key]!!)))
        }
        val sutGetWeatherInfoInParalleUseCase = GetWeatherInfoInParalleUseCase(mockWeatherRepo,testDispatcher)
        val result = sutGetWeatherInfoInParalleUseCase.invoke(mockReqRes.keys.toList()).toList()
        val domainWrapper = result.first() as DomainWrapper.Success
        Assert.assertEquals(mockReqRes.values.map { it.toWeatherEntity() }, domainWrapper.entity)
        Assert.assertEquals(1, result.size)
    }
}