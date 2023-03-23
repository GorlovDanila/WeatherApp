package com.example.weatherapp.di

//import android.content.Context
//import com.example.weatherapp.data.weather.WeatherRepositoryImpl
//import com.example.weatherapp.data.location.LocationDataSource
//import com.example.weatherapp.domain.location.GetLocationUseCase
//import com.example.weatherapp.domain.weather.GetCitiesWeatherUseCase
//import com.example.weatherapp.domain.weather.GetWeatherUseCase
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import dagger.Provides

//object DataContainer {

//    private val loggingInterceptor =
//        HttpLoggingInterceptor().apply {
//            level = if (BuildConfig.DEBUG) {
//                HttpLoggingInterceptor.Level.BODY
//            } else {
//                HttpLoggingInterceptor.Level.NONE
//            }
//        }
//
//    private val httpClient by lazy {
//        OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .addInterceptor(ApiKeyInterceptor())
//            .addInterceptor(MetricInterceptor())
//            .connectTimeout(10L, TimeUnit.SECONDS)
//            .build()
//    }
//
//    private val retrofit by lazy {
//        Retrofit.Builder()
//            .client(httpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl(BASE_URL)
//            .build()
//    }
//
//    private val weatherApi: WeatherApi = retrofit.create(WeatherApi::class.java)

//    private val weatherRepositoryImpl = WeatherRepositoryImpl(weatherApi)
//
//    val weatherUseCase: GetWeatherUseCase
//        get() = GetWeatherUseCase(weatherRepositoryImpl)
//
//    val citiesWeatherUseCase: GetCitiesWeatherUseCase
//        get() = GetCitiesWeatherUseCase(weatherRepositoryImpl)

//    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
//
//    @Provides
//    fun provideFusedLocation(
//        applicationContext: Context
//    ) {
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)
//    }
//
//    private val locationDataSource: LocationDataSource
//        get() = LocationDataSource(fusedLocationProviderClient ?: throw NullPointerException("fusedLocation is null"))
//
//    val locationUseCase: GetLocationUseCase
//        get() = GetLocationUseCase(locationDataSource)
//}
