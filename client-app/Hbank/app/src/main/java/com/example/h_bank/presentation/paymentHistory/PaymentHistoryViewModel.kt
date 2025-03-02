package com.example.h_bank.presentation.paymentHistory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bank.data.Account
import com.example.h_bank.data.Payment
import com.example.h_bank.data.PaymentTypeFilter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

class PaymentHistoryViewModel : ViewModel() {
    private val _state = MutableStateFlow(PaymentHistoryState())
    val state: StateFlow<PaymentHistoryState> = _state

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
                val date = LocalDate.ofInstant(
                    java.time.Instant.ofEpochMilli(utcTimeMillis),
                    ZoneId.systemDefault()
                )
                return !date.isAfter(LocalDate.now())
            }

            override fun isSelectableYear(year: Int): Boolean {
                return year in 2000..2100
            }
        }
    )

    private val _navigationEvent = MutableSharedFlow<PaymentHistoryNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        _state.update { it.copy(filteredPayments = state.value.allPayments) }
    }

    fun showAccountsSheet() {
        _state.update { it.copy(isAccountsSheetVisible = true) }
    }

    fun hideAccountsSheet() {
        _state.update { it.copy(isAccountsSheetVisible = false) }
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
            LocalDate.ofInstant(
                java.time.Instant.ofEpochMilli(it),
                ZoneId.systemDefault()
            )
        }
        val endDate = endDateMillis?.let {
            LocalDate.ofInstant(
                java.time.Instant.ofEpochMilli(it),
                ZoneId.systemDefault()
            )
        }

        _state.value = _state.value.copy(selectedDateRange = startDate to endDate)
        filterPayments()
    }

    fun resetFilters() {
        _state.value = _state.value.copy(
            selectedAccount = null,
            selectedType = PaymentTypeFilter.All,
            selectedDateRange = null to null,
            filteredPayments = _state.value.allPayments
        )
    }

    private fun filterPayments() {
        val filtered = _state.value.allPayments.filter { payment ->
            val accountMatch = _state.value.selectedAccount == null ||
                    payment.account == _state.value.selectedAccount

            val typeMatch = when (val selectedType = _state.value.selectedType) {
                is PaymentTypeFilter.All -> true
                is PaymentTypeFilter.Specific -> payment.type == selectedType.type
            }

            val dateMatch = _state.value.selectedDateRange.let { (start, end) ->
                if (start == null || end == null) true
                else {
                    payment.date in start..end
                }
            }

            accountMatch && typeMatch && dateMatch
        }
        _state.update { it.copy(filteredPayments = filtered) }
    }

    fun onAccountClicked(account: Account) {
        _state.update { it.copy(selectedAccount = account) }
        filterPayments()
    }

    fun onPaymentClicked(payment: Payment) {
        viewModelScope.launch {
            _navigationEvent.emit(
                PaymentHistoryNavigationEvent.NavigateToTransactionInfo(
                    payment.id
                )
            )
        }
    }

    fun onTypeClicked(type: PaymentTypeFilter) {
        _state.update { it.copy(selectedType = type) }
        filterPayments()
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(PaymentHistoryNavigationEvent.NavigateBack)
        }
    }
}