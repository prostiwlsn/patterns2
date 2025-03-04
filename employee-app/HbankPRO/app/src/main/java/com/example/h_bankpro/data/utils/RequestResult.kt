package com.example.h_bankpro.data.utils

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