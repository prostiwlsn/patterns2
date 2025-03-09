package com.example.h_bank.data.utils

sealed interface RequestResult<T> {
    data class Success<T>(
        val data: T,
    ) : RequestResult<T>

    data class Error<T>(
        val code: Int?,
        val message: String?,
    ) : RequestResult<T>

    class NoInternetConnection<T> : RequestResult<T>
}

inline fun <T, R> RequestResult<T>.mapSuccess(transform: (T) -> R): RequestResult<R> {
    return when (this) {
        is RequestResult.Success -> RequestResult.Success(transform(this.data))
        is RequestResult.Error -> RequestResult.Error(this.code, this.message)
        is RequestResult.NoInternetConnection -> RequestResult.NoInternetConnection()
    }
}