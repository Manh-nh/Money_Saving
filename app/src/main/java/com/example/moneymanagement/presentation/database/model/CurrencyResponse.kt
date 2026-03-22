package com.example.moneymanagement.presentation.database.model

import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    @SerializedName("result")
    val result: String,
    @SerializedName("base_code")
    val baseCode: String,
    @SerializedName("time_last_update_unix")
    val timeLastUpdateUnix: Long,
    @SerializedName("conversion_rates")
    val conversionRates: Map<String, Double>
)
