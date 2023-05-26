package com.example.weatherapp.presentation.presenters

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.domain.weather.GetWeatherUseCase
import com.example.weatherapp.domain.weather.WeatherInfo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    @MockK
    lateinit var getWeatherUseCase: GetWeatherUseCase

    private val cityName = "Test City"

    private lateinit var viewModel: DetailsViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = DetailsViewModel(
            getWeatherUseCase,
            cityName
        )
    }

    @Test
    fun onCallLoadWeather() = runTest {
        // arrange
        val expectedData: WeatherInfo = mockk()
        coEvery { getWeatherUseCase.invoke(cityName) } returns expectedData

        // act
        viewModel.loadWeather()

        // assert
        assertEquals(expectedData, viewModel.weatherInfo.value)
    }
}
