package com.example.weatherapp.di

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.core.interceptor.ApiKeyInterceptor
import com.example.weatherapp.data.core.interceptor.MetricInterceptor
import com.example.weatherapp.data.weather.datasource.remote.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    fun provideApiKeyInterceptor(): ApiKeyInterceptor = ApiKeyInterceptor()

    @Provides
    fun provideMetricInterceptor(): MetricInterceptor = MetricInterceptor()

    @Provides
    fun provideHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        apiKeyInterceptor: ApiKeyInterceptor,
        metricInterceptor: MetricInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(metricInterceptor)
            .connectTimeout(10L, TimeUnit.SECONDS)
            .build()

    @Provides
    fun provideRetrofit(
        httpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        @Named("base_url") baseUrl: String,
    ): Retrofit =
        Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(baseUrl)
            .build()

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun provideWeatherApi(
        retrofit: Retrofit
    ): WeatherApi = retrofit.create(WeatherApi::class.java)

    @Provides
    @Named("base_url")
    fun providesBaseUrl(): String = BuildConfig.API_ENDPOINT
}
