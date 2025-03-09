package com.example.h_bank.presentation.paymentHistory

import android.os.Build
import android.util.Range
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.core.util.toRange
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.h_bank.R
import com.example.h_bank.presentation.loanPayment.components.LoanPaymentBottomSheetContent
import com.example.h_bank.presentation.paymentHistory.components.FilterButton
import com.example.h_bank.presentation.paymentHistory.components.OperationItem
import com.example.h_bank.presentation.paymentHistory.components.PaymentHistoryHeader
import com.example.h_bank.presentation.paymentHistory.components.PaymentItem
import com.example.h_bank.presentation.paymentHistory.components.TypeBottomSheetContent
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
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
    val interactionSource = remember { MutableInteractionSource()  }
    val operationsPagingItems = state.operationsPager.collectAsLazyPagingItems()

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(
                    start = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filterModel = state.filterModel

            filterModel.getFiltersList().forEach {
                FilterButton(
                    text = it.getText(),
                    onClick = { viewModel.onFilterClick(it) },
                    isCloseable = it.isCloseable(),
                    isActive = it.isChecked(),
                    onClose = { viewModel.onFilterClose(it) }
                )
            }
            if (filterModel.isFilterSelected()) {
                Text(
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource,
                        ) {
                            viewModel.resetFilters()
                        },
                    text = stringResource(R.string.reset_filters),
                    color = Color(0xFF5C49E0),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }

        // Список операций
        when (operationsPagingItems.loadState.refresh) {
            is LoadState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = Color(0xFF5C49E0)
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    items(operationsPagingItems.itemCount) { index ->
                        val operation = operationsPagingItems[index]
                        if (operation != null) {
                            OperationItem(
                                operation = operation,
                                onClick = { /*viewModel.onOperationClicked(operation)*/ }
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = Color(0xFFD9D9D9)
                            )
                        } else {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
        }
    }
    // Выбор типа операции
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

    // Выбор счета
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

    // Календарь
    val calendarState = rememberSheetState(
        onDismissRequest = {
            viewModel.hideDatePicker()
        },
        onCloseRequest = {
            viewModel.hideDatePicker()
        }
    )

    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
            yearSelection = true,
            monthSelection = true
        ),
        selection = CalendarSelection.Period(
            selectedRange = state.startDate?.let { start ->
                state.endDate?.let { end ->
                    (start..end)
                }
            }?.toRange(),
            onSelectRange = { startDate, endDate ->
                viewModel.onDateRangeSelected(startDate.atStartOfDay(), endDate.atStartOfDay())
            },
        ),
    )

    if (state.isDatePickerVisible) {
        calendarState.show()
    }
}