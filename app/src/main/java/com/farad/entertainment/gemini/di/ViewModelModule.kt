package com.farad.entertainment.gemini.di

import com.farad.entertainment.gemini.ui.viewModel.GeminiViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::GeminiViewModel)
}