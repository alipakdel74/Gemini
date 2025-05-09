package com.farad.entertainment.gemini.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BGemini(
    val contents: List<BContent>
)

@JsonClass(generateAdapter = true)
data class BContent(
    val parts: List<BPart>
)

@JsonClass(generateAdapter = true)
data class BPart(
    val text: String
)
