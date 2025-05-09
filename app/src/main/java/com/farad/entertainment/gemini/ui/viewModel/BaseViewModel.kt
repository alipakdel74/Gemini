package com.farad.entertainment.gemini.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel(), CoroutineScope {

    private val parentJob = Job()
    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            launch {
                throwable.message?.apply {
                    Log.i("background_error", this)
                }
            }
        }

    override val coroutineContext: CoroutineContext =
        Dispatchers.Main + parentJob + coroutineExceptionHandler

    override fun onCleared() {
        parentJob.complete()
        super.onCleared()
    }
}