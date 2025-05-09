package com.farad.entertainment.gemini.api.network

import kotlinx.coroutines.flow.FlowCollector
import java.net.HttpURLConnection

abstract class ApiExecutor {

    protected suspend fun <T> FlowCollector<ApiResponse<T>>.emitApiResponse(request: suspend () -> DataResult<T>) {
        emit(ApiResponse.Loading)

        val apiResponse = executeApi(request)
        emit(apiResponse)
        emit(ApiResponse.Loaded)
    }

    private suspend fun <T> executeApi(request: suspend () -> DataResult<T>): ApiResponse<T> {
        return try {
            create(request())
        } catch (t: Throwable) {
            t.printStackTrace()
            if (t.message?.lowercase()?.contains("canceled") == true)
                ApiResponse.CancelResponse
            else create(t)
        }
    }

    private fun <T> create(error: Throwable): ApiResponse<T> =
        ApiResponse.ErrorConnection(error.getApiError(), error.message)

    private fun <T> create(response: DataResult<T>): ApiResponse<T> {
        return if (response.isSuccessful) {
            if (response.code() == HttpURLConnection.HTTP_OK)
                ApiResponse.Success(response.body())
            else ApiResponse.Error(response.code(), response.message())
        } else {
            when (response.code()) {
                HttpURLConnection.HTTP_UNAUTHORIZED, HttpURLConnection.HTTP_FORBIDDEN ->
                    ApiResponse.ExpireToken(response.code(),response.message())

                HttpURLConnection.HTTP_INTERNAL_ERROR, HttpURLConnection.HTTP_BAD_REQUEST -> {
                    val errorBytes = response.errorBody()?.bytes()
                    if (errorBytes != null) {
                        val errorContent = String(errorBytes)
                        ApiResponse.Error(
                            response.code(),
                            errorContent
                        )
                    } else
                        ApiResponse.Error(response.code(), response.message().orEmpty())
                }

                else -> ApiResponse.Error(response.code(), response.message().orEmpty())
            }
        }
    }

}