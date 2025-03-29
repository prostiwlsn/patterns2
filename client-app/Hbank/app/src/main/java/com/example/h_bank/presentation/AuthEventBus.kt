package com.example.h_bank.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AuthEventBus {
    private val _authEvents = MutableSharedFlow<Pair<String, String>>(replay = 0)
    val authEvents = _authEvents.asSharedFlow()

    suspend fun emitTokens(accessToken: String, refreshToken: String) {
        _authEvents.emit(accessToken to refreshToken)
    }
}