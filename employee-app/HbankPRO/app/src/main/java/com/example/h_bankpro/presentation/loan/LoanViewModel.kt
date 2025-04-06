package com.example.h_bankpro.presentation.loan

import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.h_bankpro.domain.useCase.GetExpiredLoanPaymentsUseCase
import com.example.h_bankpro.domain.useCase.PushCommandUseCase
import com.example.h_bankpro.presentation.common.viewModel.BaseViewModel
import com.example.h_bankpro.presentation.user.ExpiredLoanPaymentsPagingSource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoanViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    private val getExpiredLoanPaymentsUseCase: GetExpiredLoanPaymentsUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {
    private val _state = MutableStateFlow(LoanState())
    val state: StateFlow<LoanState> = _state

    private val _navigationEvent = MutableSharedFlow<LoanNavigationEvent>()
    val navigationEvent: SharedFlow<LoanNavigationEvent> = _navigationEvent

    private val loanId: String = checkNotNull(savedStateHandle["loanId"])
    private val userId: String = checkNotNull(savedStateHandle["userId"])
    private val documentNumber: String = checkNotNull(savedStateHandle["documentNumber"])
    private val amount: String = checkNotNull(savedStateHandle["amount"])
    private val endDate: String = checkNotNull(savedStateHandle["endDate"])
    private val ratePercent: String = checkNotNull(savedStateHandle["ratePercent"])
    private val debt: String = checkNotNull(savedStateHandle["debt"])

    init {
        _state.update {
            it.copy(
                documentNumber = documentNumber,
                amount = amount,
                endDate = endDate,
                ratePercent = ratePercent,
                debt = debt
            )
        }
        viewModelScope.launch {
            val pager = Pager(
                config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                pagingSourceFactory = {
                    ExpiredLoanPaymentsPagingSource(
                        loanId = loanId,
                        userId = userId,
                        getExpiredLoanPaymentsUseCase = getExpiredLoanPaymentsUseCase
                    )
                }
            ).flow.cachedIn(viewModelScope)
            _state.update {
                it.copy(
                    expiredPayments = pager
                )
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(LoanNavigationEvent.NavigateBack)
        }
    }
}