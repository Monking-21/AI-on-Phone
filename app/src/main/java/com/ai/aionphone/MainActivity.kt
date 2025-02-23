package com.ai.aionphone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ai.aionphone.ui.theme.AIonPhoneTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.ai.aionphone.data.ChatMessage
import com.ai.aionphone.data.ChatRequest
import com.ai.aionphone.network.ApiClient
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope

// 添加这些导入语句
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
// 在其他导入语句下添加
import com.ai.aionphone.ui.SettingsScreen

// 修改导入语句，添加 CenterAlignedTopAppBar
import androidx.compose.material3.CenterAlignedTopAppBar

// 添加实验性 API 的导入
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.CenterAlignedTopAppBar

import androidx.compose.material3.SmallTopAppBar

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初始化 ApiClient
        ApiClient.init(this)
        
        enableEdgeToEdge()
        setContent {
            var showSettings by remember { mutableStateOf(false) }
            
            AIonPhoneTheme {
                if (showSettings) {
                    SettingsScreen(
                        onNavigateBack = {
                            showSettings = false
                            // 重新初始化 ApiClient 以应用新设置
                            ApiClient.init(this)
                        }
                    )
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = MaterialTheme.colorScheme.background,
                        topBar = {
                            SmallTopAppBar(
                                title = { Text("AI Chat") },
                                actions = {
                                    IconButton(onClick = { showSettings = true }) {
                                        Icon(
                                            imageVector = Icons.Default.Settings,
                                            contentDescription = "Settings"
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.smallTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    ) { innerPadding ->
                        TextInputScreen(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInputScreen(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<Message>()) }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp)
            .verticalScroll(scrollState)
    ) {
        messages.forEach { message ->
            Text(
                text = when (message.type) {
                    MessageType.USER -> "You: ${message.content}"
                    MessageType.AI -> "AI: ${message.content}"
                },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = text,
            onValueChange = { newText -> text = newText },
            label = { Text("Enter text") },
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    // 使用 coroutineScope 替代 lifecycleScope
                    coroutineScope.launch {
                        messages = messages + Message(text, MessageType.USER)
                        val aiResponse = getAIResponse(text)
                        messages = messages + Message(aiResponse, MessageType.AI)
                        text = ""
                    }
                }
            }
        ) {
            Text("Submit")
        }
    }
}

// 消息类型枚举
enum class MessageType {
    USER,
    AI
}

// 消息数据类
data class Message(
    val content: String,
    val type: MessageType
)

// 在 getAIResponse 函数中修改 ApiClient 的调用方式
private suspend fun getAIResponse(userInput: String): String {
    return try {
        val request = ChatRequest(
            messages = listOf(
                ChatMessage(
                    role = "user",
                    content = userInput
                )
            )
        )

        val response = ApiClient.service.getChatCompletion(request)
        response.choices.firstOrNull()?.message?.content ?: "Sorry, I couldn't process that."
    } catch (e: Exception) {
        "Sorry, an error occurred: ${e.message}"
    }
}