package com.example.moneymanagement.presentation.database.api

import com.example.moneymanagement.presentation.database.model.GeminiRequest
import com.example.moneymanagement.presentation.database.model.GeminiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiRepository(private val api: GeminiApi) {

    suspend fun generateContent(apiKey: String, request: GeminiRequest): Result<GeminiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.generateContent(apiKey, request)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
