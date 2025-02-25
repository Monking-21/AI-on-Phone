package com.ai.aionphone.network

import com.ai.aionphone.data.OllamaRequest
import com.ai.aionphone.data.OllamaResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface OllamaService {
    @POST("api/generate")
    suspend fun generateCompletion(@Body request: OllamaRequest): OllamaResponse
}