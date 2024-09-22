package com.example.oyama

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://7g703ccxk8.execute-api.eu-north-1.amazonaws.com/prod/" // Base URL

    private val apiKey = BuildConfig.AWS_API_KEY // API Key from build.gradle

    // Logging Interceptor for debugging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient with interceptors
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Add logging interceptor
        .addInterceptor(ApiInterceptor(apiKey)) // Add API key interceptor
        .build()

    // Retrofit instance
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // API service instance
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
