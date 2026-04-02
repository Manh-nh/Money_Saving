package com.example.moneymanagement.presentation.database.model

import com.google.gson.annotations.SerializedName

data class GeminiRequest(
    @SerializedName("contents") val contents: List<GeminiContent>
)

data class GeminiContent(
    @SerializedName("role") val role: String,
    @SerializedName("parts") val parts: List<GeminiPart>
)

data class GeminiPart(
    @SerializedName("text") val text: String
)

data class GeminiSystemInstruction(
    @SerializedName("parts") val parts: List<GeminiPart>
)

data class GeminiResponse(
    @SerializedName("candidates") val candidates: List<GeminiCandidate>
)

data class GeminiCandidate(
    @SerializedName("content") val content: GeminiContent,
    @SerializedName("finishReason") val finishReason: String?
)
