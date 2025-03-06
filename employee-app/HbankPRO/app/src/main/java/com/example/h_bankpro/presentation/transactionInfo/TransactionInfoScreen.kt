package com.example.h_bankpro.presentation.transactionInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.h_bankpro.R
import com.example.h_bankpro.data.OperationType
import com.example.h_bankpro.presentation.common.SuccessIcon
import com.example.h_bankpro.presentation.common.TextField
import com.example.h_bankpro.presentation.transactionInfo.components.TransactionInfoHeader
import kotlinx.datetime.toJavaLocalDateTime
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TransactionInfoScreen(
    navController: NavController,
    viewModel: TransactionInfoViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale("ru"))
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale("ru"))

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                TransactionInfoNavigationEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        TransactionInfoHeader(
            onBackClick = { viewModel.onToMainClicked() }
        )
        Spacer(modifier = Modifier.height(70.dp))
        SuccessIcon()
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = when {
                state.operation?.operationType == OperationType.TRANSFER -> {
                    if (state.operation?.directionToMe == true) stringResource(R.string.incoming_transfer)
                    else stringResource(R.string.outgoing_transfer)
                }

                else -> state.operation?.operationType?.displayName ?: ""
            },
            fontSize = 32.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = state.operation?.amount.toString() + " ₽",
            fontSize = 30.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = state.operation?.transactionDateTime?.toJavaLocalDateTime()
                ?.let { it.format(dateFormatter) + ", " + it.format(timeFormatter) } ?: "",
            fontSize = 16.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(80.dp))

        when (state.operation?.operationType) {
            OperationType.REPLENISHMENT -> {
                TextField(
                    labelRes = R.string.replenishment_account,
                    value = state.operation?.recipientAccountId.toString(),
                )
            }

            OperationType.WITHDRAWAL -> {
                TextField(
                    labelRes = R.string.withdrawal_account,
                    value = state.operation?.senderAccountId.toString(),
                )
            }

            OperationType.TRANSFER -> {
                TextField(
                    labelRes = R.string.sender_account,
                    value = state.operation?.senderAccountId.toString(),
                )
                Spacer(modifier = Modifier.height(6.dp))
                TextField(
                    labelRes = R.string.recipient_account,
                    value = state.operation?.recipientAccountId ?: "",
                )
                Spacer(modifier = Modifier.height(6.dp))
                state.operation?.message?.let {
                    TextField(
                        labelRes = R.string.comment,
                        value = it,
                    )
                }
            }

            OperationType.LOAN_REPAYMENT -> {
                TextField(
                    labelRes = R.string.payment_account,
                    value = state.operation?.senderAccountId.toString(),
                )
            }

            null -> {}
        }
    }
}