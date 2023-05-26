package com.example.weatherapp.domain.weather

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class GetWeatherUseCaseTest {

    @MockK
    lateinit var weatherRepository: WeatherRepository

    private lateinit var useCase: GetWeatherUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetWeatherUseCase(weatherRepository = weatherRepository)
    }

    @Test
    fun whenGetWeatherUseCaseExpectedSuccess() {
        // arrange
        val requestQuery = "Kazan"
        val expectedTemp = 123.0
        val expectedData: WeatherInfo = mockk {
            every { temp } returns expectedTemp
            every { humidity } returns 500
        }
        coEvery {
            weatherRepository.getWeather(requestQuery)
        } returns expectedData
        // act
        runTest {
            val result = useCase.invoke(query = requestQuery)

            // assert
            assertEquals(expectedData, result)
            assertEquals(expectedTemp, result.temp)

        }
    }

    @Test
    fun whenGetWeatherUseCaseExpectedError() {
        // arrange
        val requestQuery = "Kazan"
        coEvery {
            weatherRepository.getWeather(requestQuery)
        } throws RuntimeException("Test")
        // act
        runTest {
            // assert
            assertFailsWith<RuntimeException> {
                useCase.invoke(query = requestQuery)
            }
        }
    }
}
