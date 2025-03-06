package com.example.h_bankpro.presentation.user

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bankpro.data.Account
import com.example.h_bankpro.data.utils.NetworkUtils.onFailure
import com.example.h_bankpro.data.utils.NetworkUtils.onSuccess
import com.example.h_bankpro.domain.useCase.BlockUserUseCase
import com.example.h_bankpro.domain.useCase.GetUserAccountsUseCase
import com.example.h_bankpro.domain.useCase.GetUserByIdUseCase
import com.example.h_bankpro.domain.useCase.UnblockUserUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(
    savedStateHandle: SavedStateHandle,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getUserAccountsUseCase: GetUserAccountsUseCase,
    private val blockUserUseCase: BlockUserUseCase,
    private val unblockUserUseCase: UnblockUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state

    private val _navigationEvent = MutableSharedFlow<UserNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val userId: String = checkNotNull(savedStateHandle["userId"])

    init {
        loadUser()
        loadUserAccounts()
    }

    private fun loadUser() {
        viewModelScope.launch {
            getUserByIdUseCase(userId)
                .onSuccess { result ->
                    _state.update { it.copy(user = result.data) }
                }
                .onFailure {
                }
        }
    }

    private fun loadUserAccounts() {
        viewModelScope.launch {
            getUserAccountsUseCase(userId)
                .onSuccess { result ->
                    _state.update { it.copy(accounts = result.data) }
                }
                .onFailure {
                }
        }
    }

    fun showAccountsSheet() {
        _state.update { it.copy(isAccountsSheetVisible = true) }
    }

    fun hideAccountsSheet() {
        _state.update { it.copy(isAccountsSheetVisible = false) }
    }

    fun showLoansSheet() {
        _state.update { it.copy(isLoansSheetVisible = true) }
    }

    fun hideLoansSheet() {
        _state.update { it.copy(isLoansSheetVisible = false) }
    }

    fun onLoanClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(UserNavigationEvent.NavigateToLoan)
        }
    }

    fun onAccountClicked(account: Account) {
        viewModelScope.launch {
            _navigationEvent.emit(
                UserNavigationEvent.NavigateToAccount(
                    account.id,
                    account.accountNumber
                )
            )
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(UserNavigationEvent.NavigateBack)
        }
    }

    fun onBlockUserClicked() {
        viewModelScope.launch {
            blockUserUseCase(userId)
                .onSuccess {
                    _state.update { currentState ->
                        currentState.copy(
                            user = currentState.user?.copy(isBlocked = true)
                        )
                    }
                }
                .onFailure {
                }
        }
    }

    fun onUnblockUserClicked() {
        viewModelScope.launch {
            unblockUserUseCase(userId)
                .onSuccess {
                    _state.update { currentState ->
                        currentState.copy(
                            user = currentState.user?.copy(isBlocked = false)
                        )
                    }
                }
                .onFailure {
                }
        }
    }
}