package com.example.moneymanagement.presentation.database.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import com.example.moneymanagement.presentation.database.model.CurrencyResponse

interface MyServiceCurrence {
    @GET("latest/{base}")
    fun getLatestRates(@Path("base") base: String): Call<CurrencyResponse>
}