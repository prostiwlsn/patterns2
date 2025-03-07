package com.example.h_bank.data.utils

import retrofit2.HttpException
import java.net.ConnectException

object NetworkUtils {
    suspend fun <T> runResultCatching(action: suspend () -> T): RequestResult<T> {
        return try {
            RequestResult.Success(
                data = action()
            )
        } catch (e: HttpException) {
            RequestResult.Error(
                code = e.code(),
                message = e.message()
            )
        } catch (e: ConnectException) {
            RequestResult.NoInternetConnection()
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