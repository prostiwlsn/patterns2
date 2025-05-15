package com.example.h_bank.presentation.main

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.h_bank.data.Account
import com.example.h_bank.data.FirebasePushManager
import com.example.h_bank.data.Loan
import com.example.h_bank.data.ThemeMode
import com.example.h_bank.data.dto.CurrencyDto
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.entity.authorization.Command
import com.example.h_bank.domain.useCase.CloseAccountUseCase
import com.example.h_bank.domain.useCase.GetCreditRatingUseCase
import com.example.h_bank.domain.useCase.GetCurrentUserUseCase
import com.example.h_bank.domain.useCase.GetUserAccountsUseCase
import com.example.h_bank.domain.useCase.GetUserIdUseCase
import com.example.h_bank.domain.useCase.OpenAccountUseCase
import com.example.h_bank.domain.useCase.SettingsUseCase
import com.example.h_bank.domain.useCase.authorization.GetMainCommandsUseCase
import com.example.h_bank.domain.useCase.authorization.LogoutUseCase
import com.example.h_bank.domain.useCase.authorization.PushCommandUseCase
import com.example.h_bank.domain.useCase.loan.GetUserLoansUseCase
import com.example.h_bank.presentation.common.viewModelBase.BaseViewModel
import com.example.h_bank.presentation.common.viewModelBase.ServerErrorException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class MainViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    private val openAccountUseCase: OpenAccountUseCase,
    private val closeAccountUseCase: CloseAccountUseCase,
    private val getUserLoansUseCase: GetUserLoansUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserAccountsUseCase: GetUserAccountsUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val settingsUseCase: SettingsUseCase,
    private val getCreditRatingUseCase: GetCreditRatingUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val firebasePushManager: FirebasePushManager,
    getAuthorizationCommandsUseCase: GetMainCommandsUseCase,
) : BaseViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<MainNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yy")

    init {
        viewModelScope.launch {
            settingsUseCase.settingsFlow.collect { settings ->
                _state.update {
                    it.copy(
                        themeMode = settings.theme,
                        hiddenAccounts = settings.hiddenAccounts
                    )
                }
            }
        }
        getAuthorizationCommandsUseCase().onEach { loadCurrentUser() }.launchIn(viewModelScope)
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

    fun showCurrenciesSheet() {
        _state.update { it.copy(isCurrenciesSheetVisible = true) }
    }

    fun hideCurrenciesSheet() {
        _state.update { it.copy(isCurrenciesSheetVisible = false) }
    }

    private fun loadCurrentUser() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                when (val result = getCurrentUserUseCase()) {
                    is RequestResult.Success -> {
                        _state.update { it.copy(currentUserId = result.data.id) }
                        loadInitialLoans()
                        loadUserAccounts()
                        loadCreditRating()
                    }

                    is RequestResult.Error -> {
                        _state.update { it.copy(isLoading = false) }
                        throw ServerErrorException()
                    }

                    is RequestResult.NoInternetConnection -> {
                        _state.update { it.copy(isLoading = false) }
                        pushCommandUseCase(Command.NavigateToNoConnection)
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                throw e
            }
        }
    }

    private fun loadInitialLoans() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                when (val result =
                    getUserLoansUseCase(state.value.currentUserId, pageNumber = 1, pageSize = 3)) {
                    is RequestResult.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                initialLoans = result.data.items
                            )
                        }
                        setupLoansPager()
                    }

                    is RequestResult.Error -> {
                        _state.update { it.copy(isLoading = false) }
                        throw ServerErrorException()
                    }

                    is RequestResult.NoInternetConnection -> {
                        _state.update { it.copy(isLoading = false) }
                        pushCommandUseCase(Command.NavigateToNoConnection)
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                throw e
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
            try {
                when (val result = getUserAccountsUseCase(state.value.currentUserId)) {
                    is RequestResult.Success -> {
                        _state.update { it.copy(accounts = result.data, isLoading = false) }
                    }

                    is RequestResult.Error -> {
                        _state.update { it.copy(isLoading = false) }
                        throw ServerErrorException()
                    }

                    is RequestResult.NoInternetConnection -> {
                        _state.update { it.copy(isLoading = false) }
                        pushCommandUseCase(Command.NavigateToNoConnection)
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                throw e
            }
        }
    }

    private fun loadCreditRating() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                when (val result = getCreditRatingUseCase(state.value.currentUserId)) {
                    is RequestResult.Success -> {
                        _state.update {
                            it.copy(
                                creditRating = result.data.creditRating,
                                isLoading = false
                            )
                        }
                    }

                    is RequestResult.Error -> {
                        _state.update { it.copy(isLoading = false) }
                        throw ServerErrorException()
                    }

                    is RequestResult.NoInternetConnection -> {
                        _state.update { it.copy(isLoading = false) }
                        pushCommandUseCase(Command.NavigateToNoConnection)
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                throw e
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

    fun onCurrencyClicked(currency: CurrencyDto) {
        viewModelScope.launch {
            try {
                when (val result = openAccountUseCase(currency)) {
                    is RequestResult.Success -> {
                        _navigationEvent.emit(MainNavigationEvent.NavigateToSuccessfulAccountOpening)
                    }

                    is RequestResult.Error -> {
                        throw ServerErrorException()
                    }

                    is RequestResult.NoInternetConnection -> {
                        pushCommandUseCase(Command.NavigateToNoConnection)
                    }
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun onTransferClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(MainNavigationEvent.NavigateToTransfer(state.value.currentUserId))
        }
    }

    fun onReplenishmentClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(
                MainNavigationEvent.NavigateToReplenishment(
                    state.value.currentUserId
                )
            )
        }
    }

    fun onWithdrawalClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(
                MainNavigationEvent.NavigateToWithdrawal(
                    state.value.currentUserId
                )
            )
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

    fun onCloseAccountClicked(account: Account) {
        viewModelScope.launch {
            try {
                when (val result = closeAccountUseCase(account.id)) {
                    is RequestResult.Success -> {
                        settingsUseCase.removeAccountFromHidden(account.id)
                        _navigationEvent.emit(
                            MainNavigationEvent.NavigateToSuccessfulAccountClosure
                        )
                    }

                    is RequestResult.Error -> {
                        throw ServerErrorException()
                    }

                    is RequestResult.NoInternetConnection -> {
                        pushCommandUseCase(Command.NavigateToNoConnection)
                    }
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val userId = getUserIdUseCase() ?: run {
                    _state.update { it.copy(isLoading = false) }
                    _navigationEvent.emit(MainNavigationEvent.NavigateToWelcome)
                    return@launch
                }
                when (val result = logoutUseCase()) {
                    is RequestResult.Success -> {
                        _state.update { it.copy(isLoading = false) }
                        _navigationEvent.emit(MainNavigationEvent.NavigateToWelcome)
                    }

                    is RequestResult.Error -> {
                        _state.update { it.copy(isLoading = false) }
                        throw ServerErrorException()
                    }

                    is RequestResult.NoInternetConnection -> {
                        _state.update { it.copy(isLoading = false) }
                        pushCommandUseCase(Command.NavigateToNoConnection)
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                throw e
            }
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            val currentTheme = settingsUseCase.settingsFlow.first().theme
            val newTheme = if (currentTheme == ThemeMode.LIGHT) ThemeMode.DARK else ThemeMode.LIGHT
            settingsUseCase.setTheme(newTheme)
        }
    }

    fun toggleAccountVisibility(accountId: String) {
        viewModelScope.launch {
            settingsUseCase.toggleAccountVisibility(accountId)
        }
    }
}