package com.example.moneymanagement.presentation.database.api

import com.example.moneymanagement.presentation.database.model.GeminiRequest
import com.example.moneymanagement.presentation.database.model.GeminiResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApi {
    // Không dùng full https:// ở đây nữa
    @POST("v1/models/gemini-1.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}
