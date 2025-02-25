package com.ai.aionphone.data

data class OllamaRequest(
    val model: String = "llama2",
    val prompt: String,
    val stream: Boolean = false
)

data class OllamaResponse(
    val model: String,
    val response: String,
    val done: Boolean
)