package com.example.h_bankpro.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bankpro.data.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state

    private val _navigationEvent = MutableSharedFlow<MainNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun showUsersSheet() {
        _state.update { it.copy(isUsersSheetVisible = true) }
    }

    fun hideUsersSheet() {
        _state.update { it.copy(isUsersSheetVisible = false) }
    }

    fun onUserClicked(user: User) {
        viewModelScope.launch {
            _navigationEvent.emit(MainNavigationEvent.NavigateToUser(user.id.toString()))
        }
    }

    fun onCreateRateClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(MainNavigationEvent.NavigateToRateCreation)
        }
    }

    fun onCreateUserClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(MainNavigationEvent.NavigateToUserCreation)
        }
    }
}