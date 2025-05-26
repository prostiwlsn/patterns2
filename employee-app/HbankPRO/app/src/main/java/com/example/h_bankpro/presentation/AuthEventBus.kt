package com.example.h_bankpro.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AuthEventBus {
    private val _authEvents = MutableSharedFlow<Pair<String?, String?>>()
    val authEvents = _authEvents.asSharedFlow()

    var isRegistration: Boolean = false

    suspend fun emitTokens(accessToken: String?, refreshToken: String?) {
        _authEvents.emit(Pair(accessToken, refreshToken))
    }
}