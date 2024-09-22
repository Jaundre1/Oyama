package com.example.oyama

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ApiInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val requestWithApiKey: Request = originalRequest.newBuilder()
            .addHeader("x-api-key", apiKey)  // Ensure the header name matches
            .build()
        return chain.proceed(requestWithApiKey)
    }
}
