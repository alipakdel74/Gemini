package com.farad.entertainment.gemini

import android.app.Application
import com.farad.entertainment.gemini.di.apiModule
import com.farad.entertainment.gemini.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GeminiApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GeminiApp)

            modules(listOf(apiModule, viewModelModule))
        }
    }
}