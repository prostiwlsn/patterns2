package com.example.h_bank.data.utils

import com.example.h_bank.presentation.common.viewModelBase.NoInternetConnectionException
import retrofit2.HttpException
import java.net.ConnectException

object NetworkUtils {
    suspend fun <T> runResultCatching(
        noConnectionCatching: Boolean = false,
        action: suspend () -> T,
    ): RequestResult<T> {
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
            if (noConnectionCatching) {
                RequestResult.NoInternetConnection<T>()
            } else {
                throw NoInternetConnectionException()
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