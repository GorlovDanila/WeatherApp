package com.example.weatherapp.data.weather

import com.example.weatherapp.data.weather.datasource.remote.WeatherApi
import com.example.weatherapp.data.weather.datasource.remote.response.CitiesResponse
import com.example.weatherapp.data.weather.datasource.remote.response.WeatherResponse
import com.example.weatherapp.domain.weather.CitiesInfo
import com.example.weatherapp.domain.weather.WeatherInfo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryImplTest {

    @MockK
    lateinit var api: WeatherApi

    private lateinit var repositoryImpl: WeatherRepositoryImpl

    private val testDispatcher: CoroutineDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repositoryImpl = WeatherRepositoryImpl(api, testDispatcher)
    }

    private val expectedWeatherResponse = mockk<WeatherResponse> {
        every { name } returns "Kazan"
        every { weather } returns listOf(
            mockk {
                every { description } returns ""
            }
        )
        every { sys } returns mockk {
            every { country } returns ""
            every { sunrise } returns 152
            every { sunset } returns 1818
        }
        every { main } returns mockk {
            every { temp } returns 10.0
            every { humidity } returns 5
            every { tempMin } returns 6.0
            every { tempMax } returns 15.0
        }
        every { wind } returns mockk {
            every { speed } returns 25.0
        }
    }

    private val expectedCitiesResponse = mockk<CitiesResponse> {
        every { cod } returns "200"
        every { count } returns 10
        every { list } returns null
        every { message } returns "test"
    }

    @Test
    fun whenCallGetWeatherExpectedSuccess() = runTest(testDispatcher) {
        // arrange
        val expectedQuery = "Kazan"
        val expectedResult = WeatherInfo(
            cityName = "Kazan",
            description = "",
            temp = 10.0,
            country = "",
            humidity = 5,
            tempMin = 6.0,
            tempMax = 15.0,
            windSpeed = 25.0,
            sunrise = 152,
            sunset = 1818
        )
        coEvery {
            api.getWeatherByName(expectedQuery)
        } returns expectedWeatherResponse
        // act
        val result = repositoryImpl.getWeather(expectedQuery)
        // assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun whenCallGetWeatherExpectedError() = runTest {
        // arrange
        val expectedQuery = "Kazan"
        coEvery {
            api.getWeatherByName(expectedQuery)
        } throws Throwable("test")
        // act
        assertFailsWith<Throwable> {
            // assert
            repositoryImpl.getWeather(expectedQuery)
        }
    }

    @Test
    fun whenCallGetNearestCitiesExpectedSuccess() = runTest(testDispatcher) {
        // arrange
        val latitude = 0.0
        val longitude = 0.0
        val expectedResult = CitiesInfo(
            cod = "200",
            count = 10,
            list = null,
            message = "test"
        )
        coEvery {
            api.getNearestCities(latitude, longitude)
        } returns expectedCitiesResponse
        // act
        val result = repositoryImpl.getNearestCities(latitude, longitude)
        // assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun whenCallGetNearestCitiesExpectedError() = runTest {
        // arrange
        val latitude = 0.0
        val longitude = 0.0
        coEvery {
            api.getNearestCities(latitude, longitude)
        } throws  Throwable("test")
        // act
        assertFailsWith<Throwable> {
            // assert
            repositoryImpl.getNearestCities(latitude, longitude)
        }
    }
}

