# AIonPhone - 移动端大语言模型调用客户端

[English](README.md)

## 项目介绍

AIonPhone 是一个 Android 应用程序，旨在让用户能够在手机上方便地调用各种大语言模型 API。支持多种接口类型，包括本地部署的模型和云端服务。

## 功能特点

- 支持多种 API 类型:
  - OpenAI 兼容接口 (如腾讯云等)
  - Ollama 本地部署模型
  - RAG (检索增强生成) 接口

- 灵活的配置选项:
  - 可自定义服务器地址
  - API Key 管理
  - 多种 API 类型切换

- 简洁的用户界面:
  - 聊天形式的交互
  - 实时响应
  - 设置界面易于使用

## 技术栈

- Kotlin
- Jetpack Compose UI
- Retrofit2 网络请求
- OkHttp3
- Material3 设计
- Coroutines 协程
- Android Architecture Components

## 使用方法

1. 安装应用后，点击右上角设置图标进入设置界面
2. 选择需要使用的 API 类型：
   - OpenAI 兼容：需要填写服务器地址和 API Key
   - Ollama：填写本地服务器地址（默认 http://localhost:11434）
   - RAG Flow：填写 RAG 服务器地址
3. 保存设置后返回主界面
4. 在输入框中输入问题，点击发送即可开始对话

## 本地部署说明

### Ollama 部署

1. 在本地机器上安装 Ollama
2. 启动 Ollama 服务
3. 在应用中配置 Ollama 服务器地址

### RAG 服务部署

1. 部署 RAG 服务器
2. 在应用中配置 RAG 服务器地址

## 注意事项

- 使用 OpenAI 兼容接口时，需要有效的 API Key
- Ollama 服务需要在同一局域网内
- 确保网络连接稳定
- 部分功能可能需要特定的权限

## 贡献指南

欢迎提交 Issue 和 Pull Request 来完善这个项目。



## 联系方式

<hanweiling@outlook.com>

---

本项目仍在持续开发中，欢迎提供建议和反馈。
