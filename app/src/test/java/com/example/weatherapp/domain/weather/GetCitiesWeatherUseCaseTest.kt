package com.example.weatherapp.domain.weather

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class GetCitiesWeatherUseCaseTest {

    @MockK
    lateinit var weatherRepository: WeatherRepository

    private lateinit var useCase: GetCitiesWeatherUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetCitiesWeatherUseCase(weatherRepository = weatherRepository)
    }

    @Test
    fun whenGetCitiesWeatherUseCaseExpectedSuccess() {
        // arrange
        val latitude = 0.0
        val longitude = 0.0
        val expectedData: CitiesInfo = mockk()
        coEvery {
            weatherRepository.getNearestCities(latitude, longitude)
        } returns expectedData
        // act
        runTest {
            val result = useCase.invoke(latitude, longitude)

            // assert
            assertEquals(expectedData, result)
        }
    }

    @Test
    fun whenGetCitiesWeatherUseCaseExpectedError() {
        // arrange
        val latitude = 0.0
        val longitude = 0.0
        coEvery {
            weatherRepository.getNearestCities(latitude, longitude)
        } throws RuntimeException()
        // act
        runTest {
            // assert
            assertFailsWith<RuntimeException> {
                useCase.invoke(latitude, longitude)
            }
        }
    }
}
