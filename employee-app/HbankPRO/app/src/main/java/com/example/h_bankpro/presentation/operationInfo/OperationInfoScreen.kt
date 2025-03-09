package com.example.h_bankpro.presentation.operationInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.h_bankpro.R
import com.example.h_bankpro.data.OperationType
import com.example.h_bankpro.presentation.common.SuccessIcon
import com.example.h_bankpro.presentation.common.TextField
import com.example.h_bankpro.presentation.operationInfo.components.OperationInfoHeader
import org.koin.androidx.compose.koinViewModel

@Composable
fun OperationInfoScreen(
    navController: NavController,
    viewModel: OperationInfoViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                OperationInfoNavigationEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
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
            Spacer(modifier = Modifier.height(46.dp))
            OperationInfoHeader(
                onBackClick = { viewModel.onToMainClicked() },
                title = state.displayTitle
            )
            Spacer(modifier = Modifier.height(70.dp))
            SuccessIcon()
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = state.formattedAmount,
                fontSize = 30.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = state.formattedDateTime,
                fontSize = 16.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(80.dp))

            when (state.operation?.operationType) {
                OperationType.REPLENISHMENT -> {
                    TextField(
                        labelRes = R.string.replenishment_account,
                        value = state.operation?.recipientAccountNumber.toString(),
                    )
                }

                OperationType.WITHDRAWAL -> {
                    TextField(
                        labelRes = R.string.withdrawal_account,
                        value = state.operation?.senderAccountNumber.toString(),
                    )
                }

                OperationType.TRANSFER -> {
                    TextField(
                        labelRes = R.string.sender_account,
                        value = state.operation?.senderAccountNumber.toString(),
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TextField(
                        labelRes = R.string.recipient_account,
                        value = state.operation?.recipientAccountNumber ?: "",
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
                        value = state.operation?.senderAccountNumber.toString(),
                    )
                }

                null -> {}
            }
        }
    }
}