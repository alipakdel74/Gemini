package com.farad.entertainment.gemini.api.network

import com.farad.entertainment.gemini.BuildConfig
import com.farad.entertainment.gemini.api.network.interceptor.CancelableInterceptor
import com.farad.entertainment.gemini.api.network.interceptor.HeaderInterceptor
import com.farad.entertainment.gemini.api.network.interceptor.LoggingInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitBuilder(
    private val moshi: Moshi,
    private val headerProvider: (() -> Map<String, String?>?)? = null

) {
    fun retrofit(): Retrofit {

        val client = OkHttpClient.Builder().apply {

            connectTimeout(API_REQ_CONNECTION_TIMEOUT_MILLIS, TimeUnit.SECONDS)
            readTimeout(API_REQ_READ_TIMEOUT_MILLIS, TimeUnit.SECONDS)
            writeTimeout(API_REQ_WRITE_TIMEOUT_MILLIS, TimeUnit.SECONDS)
            addInterceptor(CancelableInterceptor())
            if (headerProvider != null)
                addInterceptor(HeaderInterceptor(headerProvider))

            if (BuildConfig.DEBUG) {
                addInterceptor(LoggingInterceptor())
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }

        }.build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }

    companion object {
        const val API_REQ_CONNECTION_TIMEOUT_MILLIS = 30L
        const val API_REQ_READ_TIMEOUT_MILLIS = 30L
        const val API_REQ_WRITE_TIMEOUT_MILLIS = 30L
    }

}