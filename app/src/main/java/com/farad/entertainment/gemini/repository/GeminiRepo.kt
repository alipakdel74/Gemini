package com.farad.entertainment.gemini.repository

import com.farad.entertainment.gemini.api.model.JGemini
import com.farad.entertainment.gemini.api.network.ApiResponse
import kotlinx.coroutines.flow.Flow

interface GeminiRepo {

    fun enqueue(key: String, text: String): Flow<ApiResponse<JGemini>>
}