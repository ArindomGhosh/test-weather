package com.example.testapplication.data

import com.example.testapplication.data.dto.Current
import com.example.testapplication.data.dto.Location
import com.example.testapplication.data.dto.WeatherDto
import com.example.testapplication.data.repo.ApiResponse
import com.example.testapplication.data.repo.WeatherRepoImpl
import com.example.testapplication.data.services.WeatherApi
import com.example.testapplication.data.services.WeatherService
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response


@RunWith(MockitoJUnitRunner::class)
class WeatherRepoTest {
    @Mock
    private lateinit var mockWetherService: WeatherService

    @Mock
    private lateinit var mockWeatherApi: WeatherApi

    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())
    private val testScope: TestScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `test if getWeatherinfo return Success on getting success response from API`() =
        testScope.runTest {
            `when`(mockWetherService.getWeatherApi()).thenReturn(mockWeatherApi)
            `when`(mockWeatherApi.getWeatherInfo(place = "Kolkata")).thenReturn(
                Response.success(
                    WeatherDto(
                        location = Location(
                            name = "Kokata",
                            lon = 22.57,
                            lat = 22.57,
                            country = "India",
                            region = "West Bengal"
                        ),
                        current = Current(
                            temp_c = 29.0,
                            temp_f = 84.2
                        )
                    )
                )
            )
            val sutWeatherRepo = WeatherRepoImpl(
                mockWetherService,
                testDispatcher,
                this
            )


            val response = sutWeatherRepo.getWeatherinfo("Kolkata").toList()
            Assert.assertTrue(response.isNotEmpty())
            MatcherAssert.assertThat(
                response.first(),
                CoreMatchers.instanceOf(ApiResponse.Success::class.java)
            )
        }


    @Test
    fun `test for getWeatherinfo return Failure on getting Failure response from API`() =
        testScope.runTest {
            `when`(mockWetherService.getWeatherApi()).thenReturn(mockWeatherApi)
            `when`(mockWeatherApi.getWeatherInfo("Kolkata")).thenReturn(
                Response.error(
                    408,
                    ResponseBody.create(
                        MediaType.parse("application/json"),
                        "Error"
                    )
                )
            )
            val sutWeatherRepo = WeatherRepoImpl(
                mockWetherService,
                testDispatcher,
                this
            )

            val response = sutWeatherRepo.getWeatherinfo("Kolkata").toList()
            MatcherAssert.assertThat(
                response.first(),
                CoreMatchers.instanceOf(ApiResponse.Failure::class.java)
            )
        }


}