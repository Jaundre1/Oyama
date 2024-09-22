package com.example.oyama

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("data")  // Just the endpoint path relative to BASE_URL
    fun sendData(@Body data: DataModel): Call<Void>
}
