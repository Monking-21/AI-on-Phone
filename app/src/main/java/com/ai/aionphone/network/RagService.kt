package com.ai.aionphone.network

import com.ai.aionphone.data.RagRequest
import com.ai.aionphone.data.RagResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RagService {
    @POST("api/rag/generate")
    suspend fun generateRagResponse(@Body request: RagRequest): RagResponse
}