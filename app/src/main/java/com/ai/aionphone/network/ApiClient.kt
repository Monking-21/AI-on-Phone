package com.ai.aionphone.network

import android.content.Context
import com.ai.aionphone.data.ApiType
import com.ai.aionphone.data.ChatMessage
import com.ai.aionphone.data.ChatRequest
import com.ai.aionphone.data.RagRequest
import com.ai.aionphone.data.RagResponse
import com.ai.aionphone.data.OllamaRequest
import com.ai.aionphone.data.OllamaResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // 添加默认值常量
    private const val DEFAULT_OPENAI_URL = "https://api.lkeap.cloud.tencent.com/v1"
    private const val DEFAULT_OLLAMA_URL = "http://localhost:11434"
    private const val DEFAULT_RAG_URL = "http://localhost:8000"

    private var tencentService: ApiService? = null
    private var ollamaService: OllamaService? = null
    private var ragService: RagService? = null
    private var currentApiType: ApiType = ApiType.OPENAI_COMPATIBLE
    
    // 添加重置方法
    private fun resetServices() {
        tencentService = null
        ollamaService = null
        ragService = null
    }

    fun init(context: Context) {
        try {
            val sharedPrefs = context.getSharedPreferences("api_settings", Context.MODE_PRIVATE)
            
            // 获取并验证 API 类型
            currentApiType = try {
                ApiType.valueOf(
                    sharedPrefs.getString("api_type", ApiType.OPENAI_COMPATIBLE.name) 
                    ?: ApiType.OPENAI_COMPATIBLE.name
                )
            } catch (e: IllegalArgumentException) {
                ApiType.OPENAI_COMPATIBLE
            }

            // 获取基础 URL，设置默认值
            val baseUrl = sharedPrefs.getString("base_url", null)?.takeIf { it.isNotEmpty() }
                ?: when (currentApiType) {
                    ApiType.OPENAI_COMPATIBLE -> DEFAULT_OPENAI_URL
                    ApiType.OLLAMA -> DEFAULT_OLLAMA_URL
                    ApiType.RAG_FLOW -> DEFAULT_RAG_URL
                }

            // 获取 API Key
            val apiKey = sharedPrefs.getString("api_key", "") ?: ""

            // 重置现有服务
            resetServices()

            // 验证 URL 格式
            if (!baseUrl.startsWith("http://") && !baseUrl.startsWith("https://")) {
                throw IllegalArgumentException("无效的 URL 格式: $baseUrl")
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder().apply {
                        if (currentApiType == ApiType.OPENAI_COMPATIBLE && apiKey.isNotEmpty()) {
                            header("Authorization", "Bearer $apiKey")
                        }
                    }.build()
                    chain.proceed(request)
                }
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            // 根据 API 类型初始化对应服务
            when (currentApiType) {
                ApiType.OPENAI_COMPATIBLE -> {
                    if (apiKey.isEmpty()) {
                        throw IllegalStateException("OpenAI Compatible API 需要 API Key")
                    }
                    tencentService = retrofit.create(ApiService::class.java)
                }
                ApiType.OLLAMA -> ollamaService = retrofit.create(OllamaService::class.java)
                ApiType.RAG_FLOW -> ragService = retrofit.create(RagService::class.java)
            }

            android.util.Log.d("ApiClient", "初始化成功: $currentApiType, URL: $baseUrl")
        } catch (e: Exception) {
            android.util.Log.e("ApiClient", "初始化失败: ${e.message}", e)
            resetServices()
            throw e
        }
    }

    fun getCurrentApiType() = currentApiType

    suspend fun sendMessage(content: String): String {
        return try {
            when (currentApiType) {
                ApiType.OPENAI_COMPATIBLE -> {
                    val service = tencentService ?: throw IllegalStateException("OpenAI Compatible API 服务未初始化")
                    val request = ChatRequest(
                        messages = listOf(ChatMessage(role = "user", content = content))
                    )
                    val response = service.getChatCompletion(request)
                    response.choices.firstOrNull()?.message?.content ?: "无响应"
                }
                ApiType.OLLAMA -> {
                    val service = ollamaService ?: throw IllegalStateException("Ollama 服务未初始化")
                    val request = OllamaRequest(prompt = content)
                    val response = service.generateCompletion(request)
                    response.response
                }
                ApiType.RAG_FLOW -> {
                    val service = ragService ?: throw IllegalStateException("RAG 服务未初始化")
                    val request = RagRequest(query = content)
                    val response = service.generateRagResponse(request)
                    buildRagResponse(response)
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("ApiClient", "API调用失败: ${e.message}", e)
            "发生错误: ${e.message}"
        }
    }

    private fun buildRagResponse(response: RagResponse): String {
        val sb = StringBuilder()
        sb.append(response.answer)
        
        if (!response.sources.isNullOrEmpty()) {
            sb.append("\n\n参考来源:\n")
            response.sources.forEachIndexed { index, source ->
                sb.append("${index + 1}. $source\n")
            }
        }

        return sb.toString()
    }
}