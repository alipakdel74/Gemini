package com.farad.entertainment.gemini.ui.viewModel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.farad.entertainment.gemini.BuildConfig
import com.farad.entertainment.gemini.api.model.JGemini
import com.farad.entertainment.gemini.api.model.WordEntry
import com.farad.entertainment.gemini.api.network.ApiResponse
import com.farad.entertainment.gemini.repository.GeminiRepo
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GeminiViewModel(
    private val application: Application,
    private val geminiRepo: GeminiRepo,
    private val moshi: Moshi
) : BaseViewModel() {

    private val _progress = MutableStateFlow(true)
    val progress = _progress.asStateFlow()

    private val _words = MutableSharedFlow<List<WordEntry>>()
    val words = _words.asSharedFlow()
    init {
        launch {
            _progress.emit(true)
            val result = withContext(Dispatchers.Default) {
                readJsonWithMoshi()
            }
            _words.emit(result)
            _progress.emit(false)
        }
    }

    private val _geminiResponse = MutableSharedFlow<ApiResponse<JGemini>>()
    val geminiResponse = _geminiResponse.asSharedFlow()
    fun geminiResponse(text: String) {
        launch {
            _progress.emit(true)
        }
        geminiRepo.enqueue(BuildConfig.apiKey, text).onEach {
            _geminiResponse.emit(it)
            if (it is ApiResponse.Loaded)
                _progress.emit(false)
        }.launchIn(viewModelScope)
    }

    private suspend fun readJsonWithMoshi(): List<WordEntry> = withContext(Dispatchers.IO) {
        val json = application.assets.open("book1_first_sheet.json")
            .bufferedReader()
            .use { it.readText() }

        val type = Types.newParameterizedType(List::class.java, WordEntry::class.java)
        val adapter = moshi.adapter<List<WordEntry>>(type)
        return@withContext adapter.fromJson(json) ?: emptyList()
    }

}