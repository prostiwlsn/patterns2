package com.example.h_bank.presentation.transactionInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.h_bank.R
import com.example.h_bank.domain.entity.filter.OperationType
import com.example.h_bank.domain.entity.filter.OperationType.Companion.getText
import com.example.h_bank.presentation.common.SuccessIcon
import com.example.h_bank.presentation.common.TextField
import com.example.h_bank.presentation.successfulTransfer.components.SuccessfulTransferHeader
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun TransactionInfoScreen(
    navController: NavController,
    viewModel: TransactionInfoViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                TransactionInfoNavigationEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    if (state.isLoading) {
        LoadingScreen()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        SuccessfulTransferHeader(
            onBackClick = { viewModel.onToMainClicked() }
        )
        Spacer(modifier = Modifier.height(70.dp))
        SuccessIcon()
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = state.operationType?.getText().orEmpty(),
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = state.amount.orEmpty() + " ₽",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = state.date.orEmpty(),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(80.dp))

        when (state.operationType) {
            OperationType.REPLENISHMENT -> {
                TextField(
                    labelRes = R.string.replenishment_account,
                    value = state.recipientAccount.orEmpty(),
                )
            }

            OperationType.WITHDRAWAL -> {
                TextField(
                    labelRes = R.string.withdrawal_account,
                    value = state.senderAccount.orEmpty(),
                )
            }

            OperationType.TRANSFER -> {
                TextField(
                    labelRes = R.string.sender_account,
                    value = state.senderAccount.orEmpty(),
                )
                Spacer(modifier = Modifier.height(6.dp))
                TextField(
                    labelRes = R.string.recipient_account,
                    value = state.recipientAccount.orEmpty(),
                )
                Spacer(modifier = Modifier.height(6.dp))
                TextField(
                    labelRes = R.string.comment,
                    value = state.comment.orEmpty(),
                )
            }

            OperationType.LOAN_REPAYMENT -> {
                TextField(
                    labelRes = R.string.payment_account,
                    value = state.senderAccount.orEmpty(),
                )
            }

            else -> Unit
        }
    }
}