package com.farad.entertainment.gemini.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JGemini(
    val candidates: List<JCandidate>
)

@JsonClass(generateAdapter = true)
data class JCandidate(
    val content: JContent
)

@JsonClass(generateAdapter = true)
data class JContent(
    val parts: List<JPart>
)

@JsonClass(generateAdapter = true)
data class JPart(
    val text: String
)
