package com.farad.entertainment.gemini.di

import com.farad.entertainment.gemini.api.network.RetrofitBuilder
import com.farad.entertainment.gemini.api.network.endpoint.GeminiApi
import com.farad.entertainment.gemini.repository.GeminiRepo
import com.farad.entertainment.gemini.repository.GeminiRepoImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {

    single<Moshi> { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }

    single {
        RetrofitBuilder(get()).retrofit()
    }

    single { get<Retrofit>().create(GeminiApi::class.java) }

    single<GeminiRepo> { GeminiRepoImpl(get()) }

}