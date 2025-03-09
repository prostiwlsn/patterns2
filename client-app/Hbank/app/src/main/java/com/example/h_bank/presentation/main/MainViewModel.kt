package com.example.h_bank.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.h_bank.data.Account
import com.example.h_bank.data.Loan
import com.example.h_bank.data.utils.NetworkUtils.onFailure
import com.example.h_bank.data.utils.NetworkUtils.onSuccess
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.useCase.CloseAccountUseCase
import com.example.h_bank.domain.useCase.GetCurrentUserUseCase
import com.example.h_bank.domain.useCase.GetUserAccountsUseCase
import com.example.h_bank.domain.useCase.authorization.LogoutUseCase
import com.example.h_bank.domain.useCase.OpenAccountUseCase
import com.example.h_bank.domain.useCase.authorization.GetAuthorizationCommandsUseCase
import com.example.h_bank.domain.useCase.loan.GetUserLoansUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class MainViewModel(
    private val openAccountUseCase: OpenAccountUseCase,
    private val closeAccountUseCase: CloseAccountUseCase,
    private val getUserLoansUseCase: GetUserLoansUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserAccountsUseCase: GetUserAccountsUseCase,
    private val logoutUseCase: LogoutUseCase,
    getAuthorizationCommandsUseCase: GetAuthorizationCommandsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state

    private val _navigationEvent = MutableSharedFlow<MainNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yy")

    init {
        getAuthorizationCommandsUseCase().onEach {
            loadCurrentUser()
        }.launchIn(viewModelScope)

        loadCurrentUser()
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

    private fun loadCurrentUser() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getCurrentUserUseCase().onSuccess { result ->
                _state.update { it.copy(currentUserId = result.data.id) }
                loadInitialLoans()
                loadUserAccounts()
            }
                .onFailure { }
        }
    }

    private fun loadInitialLoans() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result =
                getUserLoansUseCase(state.value.currentUserId, pageNumber = 1, pageSize = 3)) {
                is RequestResult.Success -> {
                    _state.update { it.copy(isLoading = false, initialLoans = result.data.items) }
                    setupLoansPager()
                }

                is RequestResult.Error -> {
                    _state.update { it.copy(isLoading = false) }
                }

                is RequestResult.NoInternetConnection -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun setupLoansPager() {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {
                LoansPagingSource(
                    getUserLoansUseCase,
                    state.value.currentUserId
                )
            }
        ).flow.cachedIn(viewModelScope)

        _state.update { it.copy(loansFlow = pager) }
    }

    private fun loadUserAccounts() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getUserAccountsUseCase(state.value.currentUserId)
                .onSuccess { result ->
                    _state.update { it.copy(accounts = result.data, isLoading = false) }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    fun onAccountClicked(account: Account) {
    }

    fun onLoanClicked(loan: Loan) {
        viewModelScope.launch {
            _navigationEvent.emit(
                MainNavigationEvent.NavigateToLoan(
                    loan.id,
                    loan.documentNumber.toString(),
                    loan.amount.toString(),
                    loan.endDate.format(formatter),
                    loan.ratePercent.toString(),
                    loan.debt.toString()
                )
            )
        }
    }

    fun onTransferClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(MainNavigationEvent.NavigateToTransfer)
        }
    }

    fun onReplenishmentClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(MainNavigationEvent.NavigateToReplenishment)
        }
    }

    fun onWithdrawalClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(MainNavigationEvent.NavigateToWithdrawal)
        }
    }

    fun onHistoryClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(MainNavigationEvent.NavigateToPaymentHistory)
        }
    }

    fun onProcessLoanClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(MainNavigationEvent.NavigateToLoanProcessing)
        }
    }

    fun onOpenAccountClicked() {
        viewModelScope.launch {
            openAccountUseCase().onSuccess {
                _navigationEvent.emit(MainNavigationEvent.NavigateToSuccessfulAccountOpening)
            }
                .onFailure { }
        }
    }

    fun onCloseAccountClicked(account: Account) {
        viewModelScope.launch {
            closeAccountUseCase(account.id).onSuccess {
                _navigationEvent.emit(MainNavigationEvent.NavigateToSuccessfulAccountClosure)
            }
                .onFailure { }
        }
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            logoutUseCase()
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _navigationEvent.emit(MainNavigationEvent.NavigateToWelcome)
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }
}