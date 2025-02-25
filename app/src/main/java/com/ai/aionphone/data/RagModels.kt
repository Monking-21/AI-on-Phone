package com.ai.aionphone.data

data class RagRequest(
    val query: String,
    val documents: List<String>? = null,
    val model: String = "default",
    val temperature: Double = 0.7,
    val max_tokens: Int = 1000
)

data class RagResponse(
    val answer: String,
    val sources: List<String>? = null,
    val relevantDocs: List<String>? = null
)