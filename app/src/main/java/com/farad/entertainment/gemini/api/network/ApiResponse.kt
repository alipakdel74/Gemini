package com.farad.entertainment.gemini.api.network

import com.squareup.moshi.JsonDataException
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

sealed class ApiResponse<out T> {

    data object Loading : ApiResponse<Nothing>()

    data class Success<T>(val body: T?) : ApiResponse<T>()

    data class Error(val code: Int?, val message: String?) : ApiResponse<Nothing>()

    data class ErrorConnection(val type: ErrorConnectionType, val message: String?) : ApiResponse<Nothing>()

    data class ExpireToken(val code: Int, val message: String?) : ApiResponse<Nothing>()

    data object Loaded : ApiResponse<Nothing>()

    data object CancelResponse : ApiResponse<Nothing>()
}

fun Throwable.getApiError(): ErrorConnectionType {
    return when (this) {
        is UnknownHostException, is ConnectException, is SocketException -> ErrorConnectionType.ERROR_DISCONNECTED
        is JsonDataException, is JSONException -> ErrorConnectionType.ERROR_PARSE
        is TimeoutException, is SocketTimeoutException -> ErrorConnectionType.ERROR_TIME_OUT
        else -> ErrorConnectionType.ERROR_UNKNOWN
    }
}

enum class ErrorConnectionType { ERROR_DISCONNECTED, ERROR_PARSE, ERROR_TIME_OUT, ERROR_UNKNOWN }
