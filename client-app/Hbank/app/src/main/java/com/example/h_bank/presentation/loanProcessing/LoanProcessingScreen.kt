package com.example.h_bank.presentation.loanProcessing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.h_bank.R
import com.example.h_bank.presentation.common.CustomDisablableButton
import com.example.h_bank.presentation.common.IconButtonField
import com.example.h_bank.presentation.loanProcessing.components.RatesBottomSheetContent
import com.example.h_bank.presentation.common.NumberInputField
import com.example.h_bank.presentation.common.TextField
import com.example.h_bank.presentation.common.utils.Keyboard
import com.example.h_bank.presentation.common.utils.keyboardAsState
import com.example.h_bank.presentation.loanProcessing.components.LoanProcessingHeader
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanProcessingScreen(
    navController: NavController,
    viewModel: LoanProcessingViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
//    val lazyPagingItems = state.tariffsFlow.collectAsLazyPagingItems()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                LoanProcessingNavigationEvent.NavigateToSuccessfulLoanProcessing -> navController.navigate(
                    "successful_loan_processing"
                )

                LoanProcessingNavigationEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    val keyboardState by keyboardAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .imePadding()
            ) {
                CustomDisablableButton(
                    enabled = state.areFieldsValid,
                    onClick = viewModel::onProcessLoanClicked,
                    textRes = R.string.process_loan
                )
                if (keyboardState == Keyboard.Closed) {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            LoanProcessingHeader(onBackClick = { viewModel.onBackClicked() })
            Spacer(modifier = Modifier.height(37.dp))
            IconButtonField(
                labelRes = R.string.rate,
                value = state.selectedRate.name,
                icon = Icons.Default.Edit,
                onIconClick = {viewModel.showRatesSheet() },
            )
            Spacer(modifier = Modifier.height(6.dp))
            NumberInputField(
                labelRes = R.string.loan_amount,
                value = state.amount?.toString().orEmpty(),
                error = state.fieldErrors?.amountError,
                suffix = " ₽",
                onValueChange = { viewModel.onAmountChange(it) }
            )
            Spacer(modifier = Modifier.height(6.dp))
            NumberInputField(
                labelRes = R.string.term,
                value = state.term?.toString().orEmpty(),
                error = state.fieldErrors?.durationError,
                suffix = " лет",
                onValueChange = { viewModel.onTermChange(it) }
            )
            Spacer(modifier = Modifier.height(6.dp))
            TextField(
                labelRes = R.string.interest_rate,
                value = state.selectedRate.interestRate.toString() + " %",
            )
            Spacer(modifier = Modifier.height(6.dp))
            TextField(
                labelRes = R.string.daily_payment,
                value = state.dailyPayment.toString() + " ₽",
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        if (state.isRatesSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.hideRatesSheet() },
                containerColor = Color(0xFFF9F9F9),
                shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
            ) {
                RatesBottomSheetContent(
                    rates = state.rates,
                    onItemClick = { rate ->
                        viewModel.onRateClicked(rate)
                        viewModel.hideRatesSheet()
                    }
                )
            }
        }
    }
}