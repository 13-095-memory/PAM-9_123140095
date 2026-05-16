package com.memory.platform_specificfeatures.config

import com.memory.platform_specificfeatures.BuildConfig

actual object ApiConfig {
    actual val geminiApiKey: String = BuildConfig.GEMINI_API_KEY
    actual val groqApiKey: String = "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"
}