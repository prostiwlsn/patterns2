package com.example.h_bank.presentation.successfulTransfer

import androidx.activity.compose.BackHandler
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
import com.example.h_bank.R
import com.example.h_bank.presentation.common.CustomButton
import com.example.h_bank.presentation.common.SuccessIcon
import com.example.h_bank.presentation.common.TextField
import com.example.h_bank.presentation.successfulTransfer.components.SuccessfulTransferHeader
import org.koin.androidx.compose.koinViewModel

@Composable
fun SuccessfulTransferScreen(
    navController: NavController,
    viewModel: SuccessfulTransferViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                SuccessfulTransferNavigationEvent.NavigateToMain ->
                    navController.navigate("main") {
                        popUpTo(
                            "main"
                        ) { inclusive = true }
                    }
            }
        }
    }

    BackHandler {
        viewModel.onToMainClicked()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        SuccessfulTransferHeader(
            onBackClick = { viewModel.onToMainClicked() }
        )
        Spacer(modifier = Modifier.weight(1f))
        SuccessIcon()
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = state.amount + " ₽",
            fontSize = 30.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.transaction),
            fontSize = 14.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.weight(1f))
        TextField(
            labelRes = R.string.payment_account,
            value = state.accountNumber,
        )
        Spacer(modifier = Modifier.height(6.dp))
        TextField(
            labelRes = R.string.beneficiary_account_id,
            value = state.beneficiaryAccountNumber,
        )
        Spacer(modifier = Modifier.weight(1f))
        CustomButton(
            onClick = viewModel::onToMainClicked,
            textRes = R.string.to_main
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}