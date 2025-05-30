package com.example.h_bankpro.presentation.account

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.h_bankpro.data.OperationTypeFilter
import com.example.h_bankpro.domain.model.OperationShort
import com.example.h_bankpro.domain.useCase.GetOperationsByAccountUseCase
import com.example.h_bankpro.domain.useCase.PushCommandUseCase
import com.example.h_bankpro.presentation.common.viewModel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneId
import java.util.Locale
import java.time.Instant as JavaInstant
import java.time.LocalDate as JavaLocalDate

class AccountViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    savedStateHandle: SavedStateHandle,
    private val getOperationsByAccountUseCase: GetOperationsByAccountUseCase
) : BaseViewModel() {
    private val _state = MutableStateFlow(AccountState())
    val state: StateFlow<AccountState> = _state

    private val accountId: String = checkNotNull(savedStateHandle["accountId"])
    private val accountNumber: String = checkNotNull(savedStateHandle["accountNumber"])
    private val currency: String = checkNotNull(savedStateHandle["currency"])
    private val _filters = MutableStateFlow(OperationsFilters())

    @OptIn(ExperimentalMaterial3Api::class)
    val dateRangePickerState = DateRangePickerState(
        initialSelectedStartDateMillis = null,
        initialSelectedEndDateMillis = null,
        initialDisplayedMonthMillis = System.currentTimeMillis(),
        yearRange = IntRange(2000, 2100),
        initialDisplayMode = DisplayMode.Picker,
        locale = Locale("ru"),
        selectableDates = object : SelectableDates {
            @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val date = JavaLocalDate.ofInstant(
                    JavaInstant.ofEpochMilli(utcTimeMillis),
                    ZoneId.systemDefault()
                )
                return !date.isAfter(JavaLocalDate.now())
            }

            override fun isSelectableYear(year: Int): Boolean {
                return year in 2000..2100
            }
        }
    )

    private val _navigationEvent = MutableSharedFlow<AccountNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private var realTimeJob: Job? = null

    init {
        _state.update {
            it.copy(
                accountId = accountId,
                accountNumber = accountNumber,
                currency = currency
            )
        }
        viewModelScope.launch {
            _filters.collectLatest { filters ->
                _state.update { it.copy(realtimeOperations = emptyList()) }
                updatePager(filters)
                loadRealTimeOperations(filters)
            }
        }
    }

    private fun updatePager(filters: OperationsFilters) {
        val pager = Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                OperationsPagingSource(
                    accountId = accountId,
                    filters = filters,
                    getOperationsByAccountUseCase = getOperationsByAccountUseCase
                )
            }
        ).flow.cachedIn(viewModelScope)
        _state.update {
            it.copy(
                operations = pager,
                selectedOperationType = filters.operationType,
                selectedDateRange = filters.dateRange
            )
        }
    }

    private fun loadRealTimeOperations(filters: OperationsFilters) {
        realTimeJob?.cancel()
        realTimeJob = viewModelScope.launch {
            getOperationsByAccountUseCase.getOperationsFlow(accountId, filters)
                .collectLatest { operation ->
                    _state.update { currentState ->
                        val updatedOps = listOf(operation) + currentState.realtimeOperations
                        currentState.copy(realtimeOperations = updatedOps)
                    }
                }
        }
    }

    fun showTypesSheet() {
        _state.update { it.copy(isTypesSheetVisible = true) }
    }

    fun hideTypesSheet() {
        _state.update { it.copy(isTypesSheetVisible = false) }
    }

    fun showDatePicker() {
        _state.update { it.copy(isDatePickerVisible = true) }
    }

    fun hideDatePicker() {
        _state.update { it.copy(isDatePickerVisible = false) }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @OptIn(ExperimentalMaterial3Api::class)
    fun onDateRangeSelected() {
        val startDateMillis = dateRangePickerState.selectedStartDateMillis
        val endDateMillis = dateRangePickerState.selectedEndDateMillis
        val startDate = startDateMillis?.let {
            Instant.fromEpochMilliseconds(it)
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
                .toJavaLocalDate()
        }
        val endDate = endDateMillis?.let {
            Instant.fromEpochMilliseconds(it)
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
                .toJavaLocalDate()
        }
        _state.update { it.copy(selectedDateRange = startDate to endDate) }
        _filters.update { it.copy(dateRange = startDate to endDate) }
    }

    fun resetFilters() {
        _filters.update { OperationsFilters() }
    }

    fun onOperationClicked(operation: OperationShort) {
        viewModelScope.launch {
            _navigationEvent.emit(
                AccountNavigationEvent.NavigateToOperationInfo(
                    accountId,
                    operation.id
                )
            )
        }
    }

    fun onTypeClicked(type: OperationTypeFilter) {
        _filters.update { it.copy(operationType = type) }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            realTimeJob?.cancel()
            getOperationsByAccountUseCase.disconnect()
            _navigationEvent.emit(AccountNavigationEvent.NavigateBack)
        }
    }

    override fun onCleared() {
        realTimeJob?.cancel()
        getOperationsByAccountUseCase.disconnect()
        super.onCleared()
    }
}