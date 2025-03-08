package com.example.h_bankpro.presentation.user

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.h_bankpro.data.Account
import com.example.h_bankpro.data.utils.NetworkUtils.onFailure
import com.example.h_bankpro.data.utils.NetworkUtils.onSuccess
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.model.Loan
import com.example.h_bankpro.domain.useCase.BlockUserUseCase
import com.example.h_bankpro.domain.useCase.GetUserAccountsUseCase
import com.example.h_bankpro.domain.useCase.GetUserByIdUseCase
import com.example.h_bankpro.domain.useCase.GetUserLoansUseCase
import com.example.h_bankpro.domain.useCase.UnblockUserUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class UserViewModel(
    savedStateHandle: SavedStateHandle,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getUserAccountsUseCase: GetUserAccountsUseCase,
    private val blockUserUseCase: BlockUserUseCase,
    private val unblockUserUseCase: UnblockUserUseCase,
    private val getUserLoansUseCase: GetUserLoansUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state

    private val _navigationEvent = MutableSharedFlow<UserNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val userId: String = checkNotNull(savedStateHandle["userId"])

    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yy")

    init {
        loadUser()
        loadInitialLoans()
        setupLoansPager()
    }

    private fun loadUser() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getUserByIdUseCase(userId)
                .onSuccess { result ->
                    _state.update { it.copy(user = result.data) }
                    loadUserAccounts()
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    private fun loadUserAccounts() {
        viewModelScope.launch {
            getUserAccountsUseCase(userId)
                .onSuccess { result ->
                    _state.update { it.copy(accounts = result.data, isLoading = false) }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    private fun loadInitialLoans() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getUserLoansUseCase(userId, pageNumber = 1, pageSize = 3)) {
                is RequestResult.Success -> {
                    _state.update { it.copy(isLoading = false, initialLoans = result.data.items) }
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
            pagingSourceFactory = { LoansPagingSource(getUserLoansUseCase, userId) }
        ).flow.cachedIn(viewModelScope)

        _state.update { it.copy(loansFlow = pager) }
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

    fun onLoanClicked(loan: Loan) {
        viewModelScope.launch {
            _navigationEvent.emit(
                UserNavigationEvent.NavigateToLoan(
                    loan.documentNumber.toString(),
                    loan.amount.toString(),
                    loan.endDate.format(formatter),
                    loan.ratePercent.toString(),
                    loan.debt.toString()
                )
            )
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