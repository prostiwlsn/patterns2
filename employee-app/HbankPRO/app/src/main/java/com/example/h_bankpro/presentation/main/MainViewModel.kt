package com.example.h_bankpro.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bankpro.data.Rate
import com.example.h_bankpro.data.utils.NetworkUtils.onFailure
import com.example.h_bankpro.domain.model.User
import com.example.h_bankpro.data.utils.NetworkUtils.onSuccess
import com.example.h_bankpro.domain.useCase.GetCurrentUserUseCase
import com.example.h_bankpro.domain.useCase.GetUsersUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val getUsersUseCase: GetUsersUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state

    private val _navigationEvent = MutableSharedFlow<MainNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        getCurrentUserId()
    }

    fun showUsersSheet() {
        _state.update { it.copy(isUsersSheetVisible = true) }
    }

    fun hideUsersSheet() {
        _state.update { it.copy(isUsersSheetVisible = false) }
    }

    fun showRatesSheet() {
        _state.update { it.copy(isRatesSheetVisible = true) }
    }

    fun hideRatesSheet() {
        _state.update { it.copy(isRatesSheetVisible = false) }
    }

    private fun loadUsers() {
        viewModelScope.launch {
            getUsersUseCase()
                .onSuccess { result ->
                    val filteredUsers = result.data.filter { user ->
                        user.id != state.value.currentUserId
                    }
                    _state.update { it.copy(users = filteredUsers, isLoading = false) }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    private fun getCurrentUserId() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getCurrentUserUseCase()
                .onSuccess { result ->
                    _state.update { it.copy(currentUserId = result.data.id) }
                    loadUsers()
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    fun onUserClicked(user: User) {
        viewModelScope.launch {
            _navigationEvent.emit(MainNavigationEvent.NavigateToUser(user.id.toString()))
        }
    }

    fun onRateClicked(rate: Rate) {
        viewModelScope.launch {
            _navigationEvent.emit(MainNavigationEvent.NavigateToRate)
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