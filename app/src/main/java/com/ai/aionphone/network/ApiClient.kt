package com.ai.aionphone.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private var apiService: ApiService? = null
    private const val DEFAULT_URL = "https://api.lkeap.cloud.example.com/v1"

    fun init(context: Context) {
        try {
            val sharedPrefs = context.getSharedPreferences("api_settings", Context.MODE_PRIVATE)
            val baseUrl = sharedPrefs.getString("base_url", DEFAULT_URL)?.takeIf { it.isNotEmpty() } ?: DEFAULT_URL
            val apiKey = sharedPrefs.getString("api_key", "")

            if (!apiKey.isNullOrEmpty() && !apiKey.startsWith("sk-")) {
                throw IllegalArgumentException("API Key must start with 'sk-'")
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                        .header("Authorization", "Bearer $apiKey")
                        .build()
                    chain.proceed(request)
                }
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("$baseUrl/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiService = retrofit.create(ApiService::class.java)
            android.util.Log.d("ApiClient", "API Client init success")
        } catch (e: Exception) {
            android.util.Log.e("ApiClient", "init failed: ${e.message}", e)
            apiService = null
        }
    }

    val service: ApiService
        get() = apiService ?: throw IllegalStateException("ApiClient is not initialized")
}