package com.example.h_bankpro.presentation.account

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.h_bankpro.R
import com.example.h_bankpro.data.OperationTypeFilter
import com.example.h_bankpro.presentation.account.components.AccountHeader
import org.koin.androidx.compose.koinViewModel
import com.example.h_bankpro.presentation.account.components.OperationItem
import com.example.h_bankpro.presentation.account.components.FilterButton
import com.example.h_bankpro.presentation.account.components.TypeBottomSheetContent
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavController,
    viewModel: AccountViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale("ru"))
    val lazyPagingItems = state.operations.collectAsLazyPagingItems()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is AccountNavigationEvent.NavigateToOperationInfo ->
                    navController.navigate(
                        "operation_info/${event.accountId}/${event.operationId}"
                    )

                is AccountNavigationEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(46.dp))
        AccountHeader(
            accountNumber = state.accountNumber,
            onBackClick = { viewModel.onBackClicked() }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.transaction_history),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF9B9CA1),
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
                    text = state.selectedOperationType.displayName,
                    onClick = { viewModel.showTypesSheet() },
                    isActive = state.selectedOperationType !is OperationTypeFilter.All
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

        when (lazyPagingItems.loadState.refresh) {
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
                    items(lazyPagingItems.itemCount) { index ->
                        val operation = lazyPagingItems[index]
                        if (operation != null) {
                            OperationItem(
                                operation = operation,
                                onClick = { viewModel.onOperationClicked(operation) }
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

                    when (lazyPagingItems.loadState.append) {
                        is LoadState.Loading -> {
                            item {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                            }
                        }

                        is LoadState.Error -> {
                            item {
                                Text(
                                    text = stringResource(R.string.loading_error),
                                    color = Color.Red,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }

                        else -> {}
                    }
                }
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
