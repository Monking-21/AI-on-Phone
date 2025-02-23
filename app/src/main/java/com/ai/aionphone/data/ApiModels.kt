package com.ai.aionphone.data

data class ChatRequest(
    val model: String = "deepseek-r1",
    val messages: List<ChatMessage>
)

data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: ChatMessage
)