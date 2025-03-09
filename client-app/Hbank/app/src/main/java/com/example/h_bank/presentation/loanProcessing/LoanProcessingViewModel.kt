package com.example.h_bank.presentation.loanProcessing

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.h_bank.data.Account
import com.example.h_bank.data.dto.Pageable
import com.example.h_bank.data.utils.NetworkUtils.onSuccess
import com.example.h_bank.domain.entity.authorization.Command
import com.example.h_bank.domain.entity.loan.TariffEntity
import com.example.h_bank.domain.useCase.GetUserAccountsUseCase
import com.example.h_bank.domain.useCase.GetUserIdUseCase
import com.example.h_bank.domain.useCase.authorization.PushCommandUseCase
import com.example.h_bank.domain.useCase.loan.GetLoanFlowUseCase
import com.example.h_bank.domain.useCase.loan.GetLoanUseCase
import com.example.h_bank.domain.useCase.loan.GetTariffListUseCase
import com.example.h_bank.domain.useCase.loan.LoanProcessingValidationUseCase
import com.example.h_bank.domain.useCase.loan.ResetLoanUseCase
import com.example.h_bank.domain.useCase.loan.UpdateLoanUseCase
import com.example.h_bank.domain.useCase.loan.paging.TariffPagingSource
import com.example.h_bank.presentation.common.viewModelBase.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoanProcessingViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    private val updateLoanUseCase: UpdateLoanUseCase,
    private val loanProcessingValidationUseCase: LoanProcessingValidationUseCase,
    private val getTariffListUseCase: GetTariffListUseCase,
    private val getUserAccountsUseCase: GetUserAccountsUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getLoanUseCase: GetLoanUseCase,
    private val resetLoanUseCase: ResetLoanUseCase,
    getLoanFlowUseCase: GetLoanFlowUseCase,
) : BaseViewModel() {
    private val _state = MutableStateFlow(LoanProcessingState())
    val state: StateFlow<LoanProcessingState> = _state

    private val _navigationEvent = MutableSharedFlow<LoanProcessingNavigationEvent>()
    val navigationEvent: SharedFlow<LoanProcessingNavigationEvent> = _navigationEvent

    init {
        val pager = Pager(
            pagingSourceFactory = {
                TariffPagingSource(getTariffListUseCase)
            },
            config = PagingConfig(pageSize = 20, enablePlaceholders = false)
        )
        viewModelScope.launch {
            _state.update {
                it.copy(rates = pager.flow)
            }
            getTariffListUseCase(Pageable(page = 1, size = 1))
                .onSuccess { firstRate ->
                    _state.update {
                        it.copy(
                            selectedRate = firstRate.data.items.first()
                        )
                    }
                }

            val userId = getUserIdUseCase()
            getUserAccountsUseCase(userId.orEmpty()).onSuccess { accounts ->
                _state.update {
                    it.copy(
                        selectedAccount = accounts.data.first(),
                        accounts = accounts.data,
                    )
                }
            }
        }
        getLoanFlowUseCase().onEach { loan ->
            _state.update {
                it.copy(
                    amount = loan.amount,
                    term = loan.duration,
                    interestRate = loan.rate,
                    dailyPayment = loan.dailyPayment?.toDouble(),
                    areFieldsValid = loan.amount != null && loan.duration != null,
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAmountChange(amountInput: String) {
        val amountValue = amountInput.toIntOrNull()

        if (amountValue != null && amountValue <= 10_000_000 || amountInput.isEmpty()) {
            updateLoanUseCase { copy(amount = amountValue) }
        }

        val currentRate = _state.value.selectedRate?.ratePercent
        val term = _state.value.term

        _state.update {
            it.copy(
                dailyPayment = getDailyPayment(amountValue, currentRate, term)
            )
        }
    }

    private fun getDailyPayment(
        amount: Int?,
        ratePercent: Double?,
        term: Int?
    ): Double? {
        val result = amount?.let { amount ->
            ratePercent?.let { rate ->
                term?.let { term ->
                    var summ = amount.toDouble()

                    repeat(term) {
                        summ *= 1 + (rate / 100)
                    }
                    summ / (365 * term)
                }
            }
        }

        return result?.let {
            (it * 100).toInt().toDouble() / 100
        }
    }

    fun onTermChange(termInput: String) {
        val termValue = termInput.toIntOrNull()

        if (termValue != null && termValue < 10 || termInput.isEmpty()) {
            updateLoanUseCase { copy(duration = termValue) }
        }

        val amount = _state.value.amount
        val ratePercent = _state.value.selectedRate?.ratePercent

        _state.update {
            it.copy(
                dailyPayment = getDailyPayment(amount, ratePercent, termValue),
            )
        }
    }

    fun onRateClicked(selectedRate: TariffEntity) {
        val amountValue = _state.value.amount
        val term = _state.value.term

        _state.update {
            it.copy(
                dailyPayment = getDailyPayment(amountValue, selectedRate.ratePercent, term),
                selectedRate = selectedRate,
            )
        }
    }

    fun onProcessLoanClicked() {
        val fieldErrors = loanProcessingValidationUseCase()

        _state.update {
            it.copy(
                fieldErrors = fieldErrors,
            )
        }

        if (fieldErrors == null) {
            viewModelScope.launch {
                getLoanUseCase(
                    accountId = _state.value.selectedAccount?.id.orEmpty(),
                    tariffId = _state.value.selectedRate?.id.orEmpty(),
                ).onSuccess {
                    pushCommandUseCase(Command.RefreshMainScreen)
                    _navigationEvent.emit(LoanProcessingNavigationEvent.NavigateToSuccessfulLoanProcessing)
                    resetLoanUseCase()
                }
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(LoanProcessingNavigationEvent.NavigateBack)
        }
    }

    fun showRatesSheet() {
        _state.update { it.copy(isRatesSheetVisible = true) }
    }

    fun onAccountClicked(account: Account) {
        _state.update {
            it.copy(
                selectedAccount = account,
            )
        }
    }

    fun showAccountSheet() {
        _state.update { it.copy(isAccountsSheetVisible = true) }
    }

    fun hideAccountsSheet() {
        _state.update { it.copy(isAccountsSheetVisible = false) }
    }

    fun hideRatesSheet() {
        _state.update { it.copy(isRatesSheetVisible = false) }
    }
}