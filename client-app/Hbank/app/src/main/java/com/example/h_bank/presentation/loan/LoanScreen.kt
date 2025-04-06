package com.example.h_bank.presentation.loan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.h_bank.R
import com.example.h_bank.presentation.common.CustomButton
import com.example.h_bank.presentation.common.TextField
import com.example.h_bank.presentation.loan.components.ExpiredLoanItem
import com.example.h_bank.presentation.loan.components.LoanHeader
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter

@Composable
fun LoanScreen(
    navController: NavController,
    viewModel: LoanViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yy")
    val lazyPagingItems = state.expiredPayments.collectAsLazyPagingItems()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is LoanNavigationEvent.NavigateToLoanPayment -> navController.navigate(
                    "loan_payment/${event.loanId}/${event.documentNumber}/${event.debt}"
                )

                is LoanNavigationEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        LoanHeader(
            onBackClick = { viewModel.onBackClicked() },
            documentNumber = state.documentNumber
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            labelRes = R.string.loan_amount,
            value = "${state.amount} ₽",
        )
        Spacer(modifier = Modifier.height(6.dp))
        TextField(
            labelRes = R.string.due_date,
            value = state.endDate.format(formatter),
        )
        Spacer(modifier = Modifier.height(6.dp))
        TextField(
            labelRes = R.string.interest_rate,
            value = "${state.ratePercent} %"
        )
        Spacer(modifier = Modifier.height(6.dp))
        TextField(
            labelRes = R.string.debt,
            value = "${state.debt} ₽"
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.expired_loan_payments),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        when (lazyPagingItems.loadState.refresh) {
            is LoadState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            else -> {
                if (lazyPagingItems.itemCount == 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Список пуст",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(lazyPagingItems.itemCount) { index ->
                            val operation = lazyPagingItems[index]
                            if (operation != null) {
                                ExpiredLoanItem(operation = operation)
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                                )
                            } else {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .align(Alignment.CenterHorizontally),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        when (lazyPagingItems.loadState.append) {
                            is LoadState.Loading -> {
                                item {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .align(Alignment.CenterHorizontally),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}