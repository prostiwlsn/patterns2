package com.example.h_bank.presentation.transfer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import com.example.h_bank.presentation.common.TextInputField
import com.example.h_bank.presentation.loanPayment.components.LoanPaymentBottomSheetContent
import com.example.h_bank.presentation.transfer.components.TransferHeader
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(
    navController: NavController,
    viewModel: TransferViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is TransferNavigationEvent.NavigateToSuccessfulTransfer ->
                    navController.navigate(
                        "successful_transfer/${event.accountNumber}" +
                                "/${event.beneficiaryAccountNumber}" +
                                "/${event.amount}"
                    )

                TransferNavigationEvent.NavigateBack -> navController.popBackStack()
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
            Spacer(modifier = Modifier.height(40.dp))
            TransferHeader(
                onBackClick = { viewModel.onBackClicked() }
            )
            Spacer(modifier = Modifier.height(37.dp))
            IconButtonField(
                labelRes = R.string.payment_account,
                value = state.selectedAccount?.accountNumber ?: "",
                icon = Icons.Default.Edit,
                onIconClick = { viewModel.showAccountsSheet() },
            )
            Spacer(modifier = Modifier.height(6.dp))
            NumberInputField(
                labelRes = R.string.beneficiary_account_id,
                value = state.beneficiaryAccountNumber,
                onValueChange = { viewModel.onBeneficiaryAccountIdChange(it) }
            )
            Spacer(modifier = Modifier.height(6.dp))
            NumberInputField(
                labelRes = R.string.amount,
                value = state.amount.orEmpty(),
                suffix = " ₽",
                onValueChange = { viewModel.onAmountChange(it) },
                errorMessageRes = if ((state.amount?.toDoubleOrNull()
                        ?: 0.0) > (state.selectedAccount?.balance ?: 0.0)
                )
                    R.string.not_enough_money else null
            )
            Spacer(modifier = Modifier.height(6.dp))
            TextInputField(
                labelRes = R.string.comment,
                keyboardOptions = KeyboardOptions(),
                value = state.comment ?: "",
                onValueChange = { viewModel.onCommentChange(it) }
            )
            Spacer(modifier = Modifier.weight(1f))
            CustomDisablableButton(
                onClick = { viewModel.onPayClicked() },
                textRes = R.string.transfer,
                enabled = state.areFieldsValid
            )
            Spacer(modifier = Modifier.height(32.dp))
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