package com.example.h_bankpro.presentation.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class WelcomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(WelcomeState())
    val state: StateFlow<WelcomeState> = _state

    private val _navigationEvent = MutableSharedFlow<WelcomeNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onLogin() {
        viewModelScope.launch {
            _navigationEvent.emit(WelcomeNavigationEvent.NavigateToLogin)
        }
    }

    fun onRegister() {
        viewModelScope.launch {
            _navigationEvent.emit(WelcomeNavigationEvent.NavigateToRegister)
        }
    }
}