package com.example.h_bank.presentation.withdrawal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.h_bank.R
import com.example.h_bank.data.dto.CurrencyDto
import com.example.h_bank.presentation.common.CustomDisablableButton
import com.example.h_bank.presentation.common.IconButtonField
import com.example.h_bank.presentation.common.NumberInputField
import com.example.h_bank.presentation.loanPayment.components.LoanPaymentBottomSheetContent
import com.example.h_bank.presentation.withdrawal.components.WithdrawalHeader
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithdrawalScreen(
    navController: NavController,
    viewModel: WithdrawalViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is WithdrawalNavigationEvent.NavigateToSuccessfulWithdrawal ->
                    navController.navigate(
                        "successful_withdrawal/" +
                                "${event.accountNumber}/" +
                                "${event.amount}/" +
                                event.currency
                    )

                WithdrawalNavigationEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(40.dp))
                WithdrawalHeader(
                    onBackClick = { viewModel.onBackClicked() }
                )
                Spacer(modifier = Modifier.height(37.dp))
                IconButtonField(
                    labelRes = R.string.withdrawal_account,
                    value = state.selectedAccount?.accountNumber ?: "",
                    icon = Icons.Default.Edit,
                    onIconClick = { viewModel.showAccountsSheet() },
                )
                Spacer(modifier = Modifier.height(6.dp))
                NumberInputField(
                    labelRes = R.string.amount,
                    value = state.amount.orEmpty(),
                    suffix = when (state.selectedAccount?.currency) {
                        CurrencyDto.RUB -> " ₽"
                        CurrencyDto.USD -> " $"
                        CurrencyDto.AMD -> " ֏"
                        null -> ""
                    },
                    onValueChange = { viewModel.onAmountChange(it) },
                    errorMessageRes = if ((state.amount?.toDoubleOrNull()
                            ?: 0.0) > (state.selectedAccount?.balance ?: 0.0)
                    )
                        R.string.not_enough_money else null
                )
                Spacer(modifier = Modifier.weight(1f))
                CustomDisablableButton(
                    onClick = viewModel::onWithdrawClicked,
                    textRes = R.string.withdraw_button,
                    enabled = state.areFieldsValid
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        if (state.isAccountsSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.hideAccountsSheet() },
                containerColor = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
            ) {
                LoanPaymentBottomSheetContent(
                    accounts = state.accounts,
                    onItemClick = { rate ->
                        viewModel.onAccountClicked(rate)
                        viewModel.hideAccountsSheet()
                    }
                )
            }
        }
    }
}