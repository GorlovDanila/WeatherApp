package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.data.location.LocationDataSource
import com.example.weatherapp.domain.location.GetLocationUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides

@Module
class LocationModule {

    @Provides
    fun provideFusedLocation(
        applicationContext: Context
    ): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)


    @Provides
    fun provideLocationDataSource(
        fusedLocationProviderClient: FusedLocationProviderClient
    ): LocationDataSource = LocationDataSource(fusedLocationProviderClient)

    @Provides
    fun provideLocationUseCase(
        locationDataSource: LocationDataSource
    ): GetLocationUseCase = GetLocationUseCase(locationDataSource)
}
