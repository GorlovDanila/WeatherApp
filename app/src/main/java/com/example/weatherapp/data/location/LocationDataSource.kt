package com.example.weatherapp.data.location

import android.annotation.SuppressLint
import com.example.weatherapp.data.location.mapper.toLocationInfo
import com.example.weatherapp.domain.location.LocationInfo
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LocationDataSource(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO,
) {

    @SuppressLint("MissingPermission")
    suspend fun getLocation(): LocationInfo = withContext(dispatcherIO) {
        fusedLocationClient.lastLocation.await().let {
            it?.toLocationInfo() ?: LocationInfo(
                latitude = 54.5299,
                longitude = 52.8039,
            )
        }
    }
}
