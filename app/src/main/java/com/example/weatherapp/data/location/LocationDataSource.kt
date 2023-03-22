package com.example.weatherapp.data.location

import android.annotation.SuppressLint
import com.example.weatherapp.data.location.mapper.toLocationInfo
import com.example.weatherapp.domain.location.LocationInfo
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.tasks.await

class LocationDataSource(
    private val fusedLocationClient: FusedLocationProviderClient
) {

    @SuppressLint("MissingPermission")
    suspend fun getLocation(): LocationInfo = fusedLocationClient.lastLocation.await().let {
        it?.toLocationInfo() ?: LocationInfo(
            latitude = 54.5299,
            longitude = 52.8039,
        )
    }
}
