package com.example.weather.utils

import com.example.weather.models.CurrentWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("weather?")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") apiKay: String,
    ) : Response<CurrentWeather>

    @GET("weather?")
    suspend fun getCurrentWeatherByCity(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") apiKay: String,
    ) : Response<CurrentWeather>

}