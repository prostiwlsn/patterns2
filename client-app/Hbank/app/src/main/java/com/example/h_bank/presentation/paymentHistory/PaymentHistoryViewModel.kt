package com.example.h_bank.presentation.paymentHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.h_bank.data.Account
import com.example.h_bank.data.utils.NetworkUtils.onSuccess
import com.example.h_bank.domain.entity.filter.AccountFilter
import com.example.h_bank.domain.entity.filter.FilterItem
import com.example.h_bank.domain.entity.filter.OperationType
import com.example.h_bank.domain.entity.filter.OperationTypeFilter
import com.example.h_bank.domain.entity.filter.PeriodFilter
import com.example.h_bank.domain.entity.payment.PaymentItemEntity
import com.example.h_bank.domain.useCase.GetUserAccountsUseCase
import com.example.h_bank.domain.useCase.GetUserIdUseCase
import com.example.h_bank.domain.useCase.filter.GetOperationsFiltersFlowUseCase
import com.example.h_bank.domain.useCase.filter.UpdateOperationsFilterUseCase
import com.example.h_bank.domain.useCase.payment.GetOperationsHistoryUseCase
import com.example.h_bank.presentation.paymentHistory.model.OperationsFilterModel
import com.example.h_bank.presentation.paymentHistory.paging.OperationsPagingSource
import com.example.h_bank.presentation.paymentHistory.utils.DateFormatter.toStringFormat
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class PaymentHistoryViewModel(
    private val getUserAccountsUseCase: GetUserAccountsUseCase,
    private val getOperationsHistoryUseCase: GetOperationsHistoryUseCase,
    private val updateOperationsFilterUseCase: UpdateOperationsFilterUseCase,
    getOperationsFiltersFlowUseCase: GetOperationsFiltersFlowUseCase,
    getUserIdUseCase: GetUserIdUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(PaymentHistoryState())
    val state: StateFlow<PaymentHistoryState> = _state

    private val _navigationEvent = MutableSharedFlow<PaymentHistoryNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        val userId = getUserIdUseCase().orEmpty()

        getOperationsFiltersFlowUseCase().onEach {
            updatePager()
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            getUserAccountsUseCase(userId)
                .onSuccess { accountsResult ->
                    _state.update {
                        it.copy(
                            accounts = accountsResult.data
                        )
                    }
                    _state.value.filterModel.updateFilter(
                        AccountFilter(accountsResult.data.first().accountNumber)
                    )
                    updateOperationsFilterUseCase { copy(accountId = accountsResult.data.first().id) }
                }

            updatePager()
        }
        _state.update { it.copy(filteredPayments = state.value.allPayments) }
    }

    private fun updatePager() {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {
                OperationsPagingSource(getOperationsHistoryUseCase)
            }
        )
        _state.update {
            it.copy(
                operationsPager = pager.flow
            )
        }
    }

    fun onFilterClick(filterItem: FilterItem) {
        when (filterItem) {
            is AccountFilter -> showAccountsSheet()
            is PeriodFilter -> showDatePicker()
            is OperationTypeFilter -> showTypesSheet()
        }
    }

    fun onFilterClose(filterItem: FilterItem) {
        val currentFilters = _state.value.filterModel.getFiltersList()

        _state.update {
            it.copy(
                filterModel = OperationsFilterModel()
            )
        }
        currentFilters.filterNot { it == filterItem }.forEach {
            _state.value.filterModel.updateFilter(it)
        }
        when (filterItem) {
            is AccountFilter -> {
                updateOperationsFilterUseCase { copy(accountId = null) }
            }
            is PeriodFilter -> {
                updateOperationsFilterUseCase {
                    copy(
                        startDate = null,
                        endDate = null,
                    )
                }
            }
            is OperationTypeFilter -> {
                updateOperationsFilterUseCase { copy(operationType = null) }
            }
        }
    }

    fun showAccountsSheet() {
        _state.update { it.copy(isAccountsSheetVisible = true) }
    }

    fun hideAccountsSheet() {
        _state.update { it.copy(isAccountsSheetVisible = false) }
    }

    private fun showTypesSheet() {
        _state.update { it.copy(isTypesSheetVisible = true) }
    }

    fun hideTypesSheet() {
        _state.update { it.copy(isTypesSheetVisible = false) }
    }

    private fun showDatePicker() {
        _state.update { it.copy(isDatePickerVisible = true) }
    }

    fun hideDatePicker() {
        _state.update { it.copy(isDatePickerVisible = false) }
    }

    fun onDateRangeSelected(startDate: LocalDateTime, endDate: LocalDateTime) {
        _state.value = _state.value.copy(
            isDatePickerVisible = false,
        )
        _state.value.filterModel.updateFilter(
            PeriodFilter(startDate.toStringFormat() + " - " + endDate.toStringFormat())
        )
        updateOperationsFilterUseCase {
            copy(
                startDate = startDate,
                endDate = endDate,
            )
        }
    }

    fun resetFilters() {
        _state.update {
            it.copy(
                filterModel = OperationsFilterModel()
            )
        }
        updateOperationsFilterUseCase {
            copy(
                accountId = null,
                startDate = null,
                endDate = null,
                operationType = null,
            )
        }
    }

    fun onAccountClicked(account: Account) {
        _state.value.filterModel.updateFilter(
            AccountFilter(
                account.accountNumber,
            )
        )
        updateOperationsFilterUseCase { copy(accountId = account.id) }
    }

    fun onPaymentClicked(payment: PaymentItemEntity) {
        viewModelScope.launch {
            _navigationEvent.emit(
                PaymentHistoryNavigationEvent.NavigateToTransactionInfo(
                    payment.id
                )
            )
        }
    }

    fun onTypeClicked(type: OperationType) {
        _state.value.filterModel.updateFilter(
            OperationTypeFilter(type)
        )
        updateOperationsFilterUseCase { copy(operationType = type) }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(PaymentHistoryNavigationEvent.NavigateBack)
        }
    }
}