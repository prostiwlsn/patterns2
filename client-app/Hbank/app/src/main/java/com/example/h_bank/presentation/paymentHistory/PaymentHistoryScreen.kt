package com.example.h_bank.presentation.paymentHistory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.h_bank.R
import com.example.h_bank.data.PaymentTypeFilter
import org.koin.androidx.compose.koinViewModel
import com.example.h_bank.presentation.loanPayment.components.LoanPaymentBottomSheetContent
import com.example.h_bank.presentation.paymentHistory.components.PaymentHistoryHeader
import com.example.h_bank.presentation.paymentHistory.components.PaymentItem
import com.example.h_bank.presentation.paymentHistory.components.FilterButton
import com.example.h_bank.presentation.paymentHistory.components.TypeBottomSheetContent
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentHistoryScreen(
    navController: NavController,
    viewModel: PaymentHistoryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale("ru"))

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is PaymentHistoryNavigationEvent.NavigateToTransactionInfo ->
                    navController.navigate("transaction_info/${event.transactionId}")

                is PaymentHistoryNavigationEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        PaymentHistoryHeader(onBackClick = { viewModel.onBackClicked() })
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterButton(
                    text = state.selectedAccount?.accountNumber ?: stringResource(R.string.all_accounts),
                    onClick = { viewModel.showAccountsSheet() },
                    isActive = state.selectedAccount != null
                )
            }
            item {
                FilterButton(
                    text = state.selectedDateRange.let { (start, end) ->
                        if (start != null && end != null) {
                            "${start.format(dateFormatter)} - ${end.format(dateFormatter)}"
                        } else {
                            stringResource(R.string.period)
                        }
                    },
                    onClick = { viewModel.showDatePicker() },
                    isActive = state.selectedDateRange.first != null && state.selectedDateRange.second != null
                )
            }
            item {
                FilterButton(
                    text = state.selectedType.displayName,
                    onClick = { viewModel.showTypesSheet() },
                    isActive = state.selectedType !is PaymentTypeFilter.All
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clickable { viewModel.resetFilters() },
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource(R.string.reset_filters),
                color = Color(0xFF5C49E0),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            items(state.filteredPayments) { payment ->
                PaymentItem(
                    payment,
                    onClick = { viewModel.onPaymentClicked(payment) }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color(0xFFD9D9D9)
                )
            }
        }
    }
    if (state.isTypesSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.hideTypesSheet() },
            containerColor = Color(0xFFF9F9F9),
            shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)
        ) {
            TypeBottomSheetContent(
                onItemClick = { type ->
                    viewModel.onTypeClicked(type)
                    viewModel.hideTypesSheet()
                }
            )
        }
    }

    if (state.isAccountsSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.hideAccountsSheet() },
            containerColor = Color(0xFFF9F9F9),
            shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)
        ) {
            LoanPaymentBottomSheetContent(
                accounts = state.accounts,
                onItemClick = { account ->
                    viewModel.onAccountClicked(account)
                    viewModel.hideAccountsSheet()
                }
            )
        }
    }

    if (state.isDatePickerVisible) {
        DatePickerDialog(
            onDismissRequest = { viewModel.hideDatePicker() },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onDateRangeSelected()
                        viewModel.hideDatePicker()
                    },
                    enabled = viewModel.dateRangePickerState.selectedStartDateMillis != null &&
                            viewModel.dateRangePickerState.selectedEndDateMillis != null
                ) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideDatePicker() }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DateRangePicker(
                state = viewModel.dateRangePickerState,
                modifier = Modifier.padding(16.dp),
                headline = {
                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale("ru"))
                    val start = viewModel.dateRangePickerState.selectedStartDateMillis?.let {
                        LocalDate.ofInstant(
                            java.time.Instant.ofEpochMilli(it),
                            ZoneId.systemDefault()
                        )
                    }
                    val end = viewModel.dateRangePickerState.selectedEndDateMillis?.let {
                        LocalDate.ofInstant(
                            java.time.Instant.ofEpochMilli(it),
                            ZoneId.systemDefault()
                        )
                    }
                    val headlineText = if (start != null && end != null) {
                        "${start.format(formatter)} - ${end.format(formatter)}"
                    } else {
                        stringResource(R.string.choose_period)
                    }
                    Text(
                        text = headlineText,
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                title = null
            )
        }
    }
}
