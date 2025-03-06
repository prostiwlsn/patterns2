package com.example.h_bankpro.presentation.account

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bankpro.data.OperationTypeFilter
import com.example.h_bankpro.data.dto.Pageable
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.model.OperationShort
import com.example.h_bankpro.domain.useCase.GetOperationsByAccountUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneId
import java.util.Locale
import java.time.LocalDate as JavaLocalDate
import java.time.Instant as JavaInstant

class AccountViewModel(
    savedStateHandle: SavedStateHandle,
    private val getOperationsByAccountUseCase: GetOperationsByAccountUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AccountState())
    val state: StateFlow<AccountState> = _state

    private val accountId: String = checkNotNull(savedStateHandle["accountId"])
    private val accountNumber: String = checkNotNull(savedStateHandle["accountNumber"])

    private val defaultPageable = Pageable(
        page = 0,
        size = 20,
        sort = listOf("transactionDateTime,desc")
    )

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

    init {
        _state.update { it.copy(accountId = accountId) }
        _state.update { it.copy(accountNumber = accountNumber) }
        loadInitialOperations()
    }

    private fun loadInitialOperations() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val accountId = state.value.accountId ?: return@launch
            when (val result = getOperationsByAccountUseCase(accountId, defaultPageable)) {
                is RequestResult.Success -> {
                    _state.update {
                        val operations = result.data.content
                        it.copy(
                            allOperations = operations,
                            filteredOperations = filterOperations(operations),
                            currentPage = result.data.number,
                            totalPages = result.data.totalPages,
                            isLoading = false
                        )
                    }
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

    fun loadNextPage() {
        viewModelScope.launch {
            val currentPage = state.value.currentPage
            val totalPages = state.value.totalPages
            if (currentPage + 1 >= totalPages || state.value.isLoading) return@launch

            _state.update { it.copy(isLoading = true) }
            val accountId = state.value.accountId ?: return@launch
            val nextPageable = defaultPageable.copy(page = currentPage + 1)
            when (val result = getOperationsByAccountUseCase(accountId, nextPageable)) {
                is RequestResult.Success -> {
                    _state.update {
                        val newOperations = it.allOperations + result.data.content
                        it.copy(
                            allOperations = newOperations,
                            filteredOperations = filterOperations(newOperations),
                            currentPage = result.data.number,
                            isLoading = false
                        )
                    }
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

    private fun filterOperations(operations: List<OperationShort>): List<OperationShort> {
        return operations.filter { operation ->
            val typeMatch = when (val selectedType = state.value.selectedOperationType) {
                is OperationTypeFilter.All -> true
                is OperationTypeFilter.Specific -> operation.operationType == selectedType.type
            }
            val dateMatch = state.value.selectedDateRange.let { (start, end) ->
                if (start == null || end == null) true
                else {
                    val operationDate = operation.transactionDateTime.date
                        .toJavaLocalDate()
                    operationDate in start..end
                }
            }
            typeMatch && dateMatch
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
        _state.update { it.copy(filteredOperations = filterOperations(it.allOperations)) }
    }

    fun resetFilters() {
        _state.update {
            it.copy(
                selectedOperationType = OperationTypeFilter.All,
                selectedDateRange = null to null,
                filteredOperations = it.allOperations
            )
        }
    }

    fun onOperationClicked(operation: OperationShort) {
        viewModelScope.launch {
            _navigationEvent.emit(
                AccountNavigationEvent.NavigateToTransactionInfo(
                    operation.id
                )
            )
        }
    }

    fun onTypeClicked(type: OperationTypeFilter) {
        _state.update { it.copy(selectedOperationType = type) }
        _state.update { it.copy(filteredOperations = filterOperations(it.allOperations)) }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(AccountNavigationEvent.NavigateBack)
        }
    }
}