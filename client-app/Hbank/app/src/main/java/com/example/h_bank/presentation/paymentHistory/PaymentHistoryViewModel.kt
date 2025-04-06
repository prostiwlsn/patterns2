package com.example.h_bank.presentation.paymentHistory

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.h_bank.data.Account
import com.example.h_bank.data.utils.NetworkUtils.onSuccess
import com.example.h_bank.domain.entity.filter.AccountFilter
import com.example.h_bank.domain.entity.filter.FilterItem
import com.example.h_bank.domain.entity.filter.OperationType
import com.example.h_bank.domain.entity.filter.OperationTypeFilter
import com.example.h_bank.domain.entity.filter.PeriodFilter
import com.example.h_bank.domain.useCase.GetUserAccountsUseCase
import com.example.h_bank.domain.useCase.GetUserIdUseCase
import com.example.h_bank.domain.useCase.authorization.PushCommandUseCase
import com.example.h_bank.domain.useCase.filter.GetOperationsFiltersFlowUseCase
import com.example.h_bank.domain.useCase.filter.UpdateOperationsFilterUseCase
import com.example.h_bank.domain.useCase.payment.GetOperationsHistoryUseCase
import com.example.h_bank.presentation.common.viewModelBase.BaseViewModel
import com.example.h_bank.presentation.paymentHistory.model.OperationShortModel
import com.example.h_bank.presentation.paymentHistory.model.OperationsFilterModel
import com.example.h_bank.presentation.paymentHistory.paging.OperationsPagingSource
import com.example.h_bank.presentation.paymentHistory.utils.DateFormatter.toStringFormat
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class PaymentHistoryViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    private val getUserAccountsUseCase: GetUserAccountsUseCase,
    private val getOperationsHistoryUseCase: GetOperationsHistoryUseCase,
    private val updateOperationsFilterUseCase: UpdateOperationsFilterUseCase,
    getOperationsFiltersFlowUseCase: GetOperationsFiltersFlowUseCase,
    getUserIdUseCase: GetUserIdUseCase,
) : BaseViewModel() {
    private val _state = MutableStateFlow(PaymentHistoryState())
    val state: StateFlow<PaymentHistoryState> = _state

    private val _navigationEvent = MutableSharedFlow<PaymentHistoryNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private var realTimeJob: Job? = null

    init {
        val userId = getUserIdUseCase().orEmpty()

        viewModelScope.launch {
            getUserAccountsUseCase(userId).onSuccess { accountsResult ->
                _state.update { it.copy(accounts = accountsResult.data) }
                val firstAccount = accountsResult.data.first()
                _state.update {
                    it.copy(
                        filterModel = it.filterModel.copy(
                            accountFilter = AccountFilter(firstAccount.accountNumber)
                        )
                    )
                }
                updateOperationsFilterUseCase { copy(accountId = firstAccount.id) }
                updatePager()
                loadRealTimeOperations()
            }
        }

        viewModelScope.launch {
            getOperationsFiltersFlowUseCase().collectLatest { filters ->
                _state.update { it.copy(operations = emptyList()) }
                updatePager()
                loadRealTimeOperations()
            }
        }
    }

    private fun updatePager() {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { OperationsPagingSource(getOperationsHistoryUseCase) }
        )
        _state.update { currentState ->
            currentState.copy(operationsPager = pager.flow)
        }
    }

    private fun loadRealTimeOperations() {
        realTimeJob?.cancel()
        realTimeJob = viewModelScope.launch {
            getOperationsHistoryUseCase.getOperationsFlow().collectLatest { operation ->
                _state.update { currentState ->
                    val updatedOps = listOf(operation) + currentState.operations
                    currentState.copy(operations = updatedOps)
                }
            }
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
        _state.update { it.copy(filterModel = OperationsFilterModel()) }
        currentFilters.filterNot { it == filterItem }.forEach {
            _state.value.filterModel.updateFilter(it)
        }
        when (filterItem) {
            is AccountFilter -> updateOperationsFilterUseCase { copy(accountId = null) }
            is PeriodFilter -> updateOperationsFilterUseCase {
                copy(
                    startDate = null,
                    endDate = null
                )
            }

            is OperationTypeFilter -> updateOperationsFilterUseCase { copy(operationType = null) }
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
        _state.update {
            it.copy(
                isDatePickerVisible = false,
                filterModel = it.filterModel.updateFilter(
                    PeriodFilter("${startDate.toStringFormat()} - ${endDate.toStringFormat()}")
                )
            )
        }
        updateOperationsFilterUseCase { copy(startDate = startDate, endDate = endDate) }
    }

    fun resetFilters() {
        _state.update {
            it.copy(
                filterModel = OperationsFilterModel().copy(
                    accountFilter = _state.value.filterModel.accountFilter
                )
            )
        }
        updateOperationsFilterUseCase {
            copy(
                startDate = null,
                endDate = null,
                operationType = null
            )
        }
    }

    fun onAccountClicked(account: Account) {
        _state.update {
            it.copy(
                filterModel = it.filterModel.updateFilter(AccountFilter(account.accountNumber))
            )
        }
        updateOperationsFilterUseCase { copy(accountId = account.id) }
    }

    fun onPaymentClicked(payment: OperationShortModel) {
        viewModelScope.launch {
            _navigationEvent.emit(PaymentHistoryNavigationEvent.NavigateToTransactionInfo(payment.id))
        }
    }

    fun onTypeClicked(type: OperationType) {
        _state.update {
            it.copy(
                filterModel = it.filterModel.updateFilter(OperationTypeFilter(type))
            )
        }
        updateOperationsFilterUseCase { copy(operationType = type) }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            realTimeJob?.cancel()
            getOperationsHistoryUseCase.disconnect()
            _navigationEvent.emit(PaymentHistoryNavigationEvent.NavigateBack)
        }
    }

    override fun onCleared() {
        realTimeJob?.cancel()
        getOperationsHistoryUseCase.disconnect()
        super.onCleared()
    }
}