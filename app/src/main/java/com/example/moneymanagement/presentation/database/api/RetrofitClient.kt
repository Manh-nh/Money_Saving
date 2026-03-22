package com.example.moneymanagement.presentation.database.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://v6.exchangerate-api.com/v6/2af50df6de860315c2278806/"

    val logging = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    val okHttp = OkHttpClient.Builder().apply {
        addInterceptor(logging)
    }

    val retrofitCurrency = Retrofit.
    Builder().baseUrl(BASE_URL).
    addConverterFactory(
        GsonConverterFactory.create())
        .client(okHttp.build()).build()
        .create(MyServiceCurrence::class.java)

}