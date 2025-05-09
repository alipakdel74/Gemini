package com.farad.entertainment.gemini.api.network.endpoint

import com.farad.entertainment.gemini.api.model.BGemini
import com.farad.entertainment.gemini.api.model.JGemini
import com.farad.entertainment.gemini.api.network.DataResult
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApi {

    @POST("v1beta/models/gemini-2.0-flash:generateContent")
    suspend fun generateContent(
        @Query("key") key: String,
        @Body contents: BGemini
    ): DataResult<JGemini>

}