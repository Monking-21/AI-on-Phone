# AIonPhone - Mobile LLM Client

[中文文档](README_zh.md)

## Introduction

AIonPhone is an Android application designed to enable users to interact with various Large Language Models (LLMs) on their mobile devices. It supports both locally deployed models and cloud services.

## Features

- Multiple API Support:
  - OpenAI-compatible APIs (e.g., Tencent Cloud)
  - Ollama Local Models
  - RAG (Retrieval-Augmented Generation) Interface

- Flexible Configuration:
  - Custom Server URLs
  - API Key Management
  - Multiple API Type Switching

- Clean User Interface:
  - Chat-like Interaction
  - Real-time Responses
  - User-friendly Settings

## Tech Stack

- Language: Kotlin
- UI Framework: Jetpack Compose
- Networking: Retrofit2, OkHttp3
- Design System: Material3
- Concurrency: Kotlin Coroutines
- Architecture: Android Architecture Components

## Getting Started

1. After installation, tap the settings icon in the top-right corner
2. Choose your preferred API type:
   - OpenAI Compatible: Requires server URL and API key
   - Ollama: Set local server URL (default: http://localhost:11434)
   - RAG Flow: Configure RAG server URL
3. Save settings and return to main screen
4. Start chatting by entering your message and tapping send

## Local Deployment

### Ollama Setup
```bash
# Download Ollama
curl -L https://ollama.com/download/windows > ollama-windows.zip
unzip ollama-windows.zip

# Start Ollama service
ollama serve

# Pull a model (example)
ollama pull llama2
```

### RAG Service Setup
```bash
# Clone RAG service repository
git clone [your-rag-service-repo]
cd [your-rag-service]

# Install dependencies
pip install -r requirements.txt

# Start service
python app.py
```

## Configuration Examples

```kotlin
// OpenAI Compatible
Base URL: https://api.lkeap.cloud.tencent.com/v1
API Key: sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

// Ollama
Base URL: http://localhost:11434

// RAG Flow
Base URL: http://localhost:8000
```

## Development Setup

```bash
# Clone repository
git clone https://github.com/yourusername/AIonPhone.git

# Open in Android Studio or VS Code
cd AIonPhone

# Build project
./gradlew build
```

## Requirements

- Android 6.0 (API level 23) or higher
- Internet connection
- Local network access for Ollama

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Contact

<hanweiling@outlook.com>

---

This project is under active development. Feedback and suggestions are welcome!

### Issues

Please report any issues on our GitHub repository's issue tracker.

