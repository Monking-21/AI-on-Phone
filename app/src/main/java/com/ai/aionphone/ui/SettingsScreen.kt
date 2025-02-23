package com.ai.aionphone.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material3.SmallTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val sharedPrefs = remember { 
        context.getSharedPreferences("api_settings", Context.MODE_PRIVATE) 
    }
    
    var baseUrl by remember { 
        mutableStateOf(
            sharedPrefs.getString("base_url", "https://api.lkeap.cloud.tencent.com/v1") ?: ""
        ) 
    }
    var apiKey by remember { 
        mutableStateOf(sharedPrefs.getString("api_key", "") ?: "") 
    }
    
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("设置") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "返回")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (showError) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            TextField(
                value = baseUrl,
                onValueChange = { 
                    baseUrl = it
                    showError = false
                },
                label = { Text("Base URL (例如: https://api.lkeap.cloud.example.com/v1)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            TextField(
                value = apiKey,
                onValueChange = { 
                    apiKey = it
                    showError = false
                },
                label = { Text("API Key (以 sk- 开头)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Button(
                onClick = {
                    try {
                        // 验证输入
                        if (baseUrl.isBlank()) {
                            throw IllegalArgumentException("Base URL cannot be empty")
                        }
                        if (!baseUrl.startsWith("https://")) {
                            throw IllegalArgumentException("Base URL must start with 'https://'")
                        }
                        if (apiKey.isBlank()) {
                            throw IllegalArgumentException("API Key cannot be empty")
                        }
                        if (!apiKey.startsWith("sk-")) {
                            throw IllegalArgumentException("API Key must start with 'sk-'")
                        }

                        // 保存设置
                        sharedPrefs.edit().apply {
                            putString("base_url", baseUrl)
                            putString("api_key", apiKey)
                            commit()
                        }
                        
                        // 记录日志
                        android.util.Log.d("SettingsScreen", "saved settings: baseUrl=$baseUrl, apiKey=$apiKey")
                        
                        onNavigateBack()
                    } catch (e: Exception) {
                        showError = true
                        errorMessage = e.message ?: "save settings failed"
                        android.util.Log.e("SettingsScreen", "save settings error: ${e.message}")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Save")
            }
        }
    }
}