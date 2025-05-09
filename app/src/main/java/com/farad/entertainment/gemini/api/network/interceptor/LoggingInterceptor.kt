package com.farad.entertainment.gemini.api.network.interceptor

import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class LoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val reqBody = request.body
        var reqNameValues = "Body: "
        var reqHeaders = "Headers: " + if (request.headers.size > 0) "" else "-"

        for (i in 0 until request.headers.size) {
            reqHeaders += "[${request.headers.name(i)}:${request.headers.value(i)}]"
        }

        if (reqBody is FormBody) {
            if (reqBody.size > 0) {
                for (i in 0 until reqBody.size) {
                    reqNameValues += "[${reqBody.name(i)}: ${reqBody.value(i)}]"
                    if (i != reqBody.size - 1) {
                        reqNameValues += ", "
                    }
                }
            } else {
                reqNameValues += "-"
            }
        }
        val response = chain.proceed(request)
        val responseBytes = response.body?.bytes()
        val responseString = responseBytes?.run { String(this) } ?: "-"
        val responseContentType = response.body?.contentType()

        //Response is consumed, so we create another one
        return response.newBuilder()
            .body(responseString.toResponseBody(responseContentType))
            .build()

    }
}