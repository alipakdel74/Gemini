package com.farad.entertainment.gemini.repository

import com.farad.entertainment.gemini.api.model.BContent
import com.farad.entertainment.gemini.api.model.BGemini
import com.farad.entertainment.gemini.api.model.BPart
import com.farad.entertainment.gemini.api.model.JGemini
import com.farad.entertainment.gemini.api.network.ApiExecutor
import com.farad.entertainment.gemini.api.network.ApiResponse
import com.farad.entertainment.gemini.api.network.endpoint.GeminiApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GeminiRepoImpl(private val geminiApi: GeminiApi) : GeminiRepo, ApiExecutor() {
    override fun enqueue(key: String, text: String): Flow<ApiResponse<JGemini>> = flow {
        emitApiResponse {
            geminiApi.generateContent(key, BGemini(listOf(BContent(listOf(BPart(text))))))
        }
    }
}