package com.example.h_bank.data.utils

import com.example.h_bank.presentation.common.viewModelBase.NoInternetConnectionException
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.net.ConnectException

object NetworkUtils {
    private const val MAX_RETRY_ATTEMPTS = 3
    private const val INITIAL_RETRY_DELAY_MS = 1000L

    suspend fun <T> runResultCatching(
        noConnectionCatching: Boolean = false,
        infiniteRetry: Boolean = false,
        action: suspend () -> T,
    ): RequestResult<T> {
        var attempt = 0
        while (true) {
            try {
                return RequestResult.Success(data = action())
            } catch (e: HttpException) {
                if (e.code() in 500..599 && (infiniteRetry || attempt < MAX_RETRY_ATTEMPTS - 1)) {
                    attempt++
//                    val delayMs = INITIAL_RETRY_DELAY_MS * (1 shl minOf(attempt, 5))
                    delay(INITIAL_RETRY_DELAY_MS)
                    continue
                }
                return RequestResult.Error(code = e.code(), message = e.message())
            } catch (e: ConnectException) {
                if (noConnectionCatching) {
                    return RequestResult.NoInternetConnection()
                } else {
                    throw NoInternetConnectionException()
                }
            }
        }
    }

    suspend fun <T> RequestResult<T>.onSuccess(
        action: suspend (RequestResult.Success<T>) -> Unit
    ): RequestResult<T> {
        if (this is RequestResult.Success<T>) {
            action(this)
        }
        return this
    }

    suspend fun <T> RequestResult<T>.onFailure(
        action: suspend (RequestResult.Error<T>) -> Unit
    ): RequestResult<T> {
        if (this is RequestResult.Error<T>) {
            action(this)
        }
        return this
    }
}