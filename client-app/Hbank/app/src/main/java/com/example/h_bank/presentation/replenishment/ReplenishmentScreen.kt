package com.example.h_bank.presentation.replenishment

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
import androidx.compose.material3.ModalBottomSheet
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
import com.example.h_bank.presentation.common.NumberInputField
import com.example.h_bank.presentation.loanPayment.components.LoanPaymentBottomSheetContent
import com.example.h_bank.presentation.replenishment.components.ReplenishmentHeader
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplenishmentScreen(
    navController: NavController,
    viewModel: ReplenishmentViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is ReplenishmentNavigationEvent.NavigateToSuccessfulReplenishment ->
                    navController.navigate(
                        "successful_replenishment/${event.accountNumber}/${event.amount}"
                    )

                ReplenishmentNavigationEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White),
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
                        color = Color(0xFF5C49E0)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(40.dp))
                ReplenishmentHeader(
                    onBackClick = { viewModel.onBackClicked() }
                )
                Spacer(modifier = Modifier.height(37.dp))
                IconButtonField(
                    labelRes = R.string.replenishment_account,
                    value = state.selectedAccount?.accountNumber ?: "",
                    icon = Icons.Default.Edit,
                    onIconClick = { viewModel.showAccountsSheet() },
                )
                Spacer(modifier = Modifier.height(6.dp))
                NumberInputField(
                    labelRes = R.string.amount,
                    value = state.amount.toString(),
                    suffix = " ₽",
                    onValueChange = { viewModel.onAmountChange(it) }
                )
                Spacer(modifier = Modifier.weight(1f))
                CustomDisablableButton(
                    onClick = viewModel::onReplenishClicked,
                    textRes = R.string.replenish_button,
                    enabled = state.areFieldsValid
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        if (state.isAccountsSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.hideAccountsSheet() },
                containerColor = Color(0xFFF9F9F9),
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