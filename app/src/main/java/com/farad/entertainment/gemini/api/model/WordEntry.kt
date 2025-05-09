package com.farad.entertainment.gemini.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WordEntry(
    val id: Int,
    val wordID: Int,
    val word: String,
    val meaning: String,
    val pronun: String,
    val example: String,
    val exampleMeaning: String,
    val state: Int,
    val dateOfLastExam: String,
    val dateOfNextExam: String,
    val example2: String,
    val exampleMeaning2: String,
    val example3: String,
    val exampleMeaning3: String,
    val fav: Int,
    val lessonNumber: Int
)
