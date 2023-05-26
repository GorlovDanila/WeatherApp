package com.example.weatherapp.domain.location

import com.example.weatherapp.data.location.LocationDataSource
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
class GetLocationUseCaseTest {

    @MockK
    lateinit var locationDataSource: LocationDataSource

    private lateinit var useCase: GetLocationUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetLocationUseCase(locationDataSource = locationDataSource)
    }

    @Test
    fun whenGetLocationUseCaseExpectedSuccess() {
        // arrange
        val expectedData: LocationInfo = mockk()
        coEvery {
            locationDataSource.getLocation()
        } returns expectedData
        // act
        runTest {
            val result = useCase.invoke()

            // assert
            assertEquals(expectedData, result)
        }
    }

    @Test
    fun whenGetLocationUseCaseExpectedError() {
        // arrange
        coEvery {
            locationDataSource.getLocation()
        } throws RuntimeException()
        // act
        runTest {
            // assert
            assertFailsWith<RuntimeException> {
                useCase.invoke()
            }
        }
    }
}
