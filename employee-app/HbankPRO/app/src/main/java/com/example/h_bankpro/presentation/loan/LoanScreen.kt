package com.example.h_bankpro.presentation.loan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.h_bankpro.R
import com.example.h_bankpro.presentation.common.TextField
import com.example.h_bankpro.presentation.loan.components.LoanHeader
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter

@Composable
fun LoanScreen(
    navController: NavController,
    viewModel: LoanViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yy")

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                LoanNavigationEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        LoanHeader(
            onBackClick = { viewModel.onBackClicked() },
//            documentNumber = state.loan.documentNumber.toString()
            documentNumber = ""
        )
        Spacer(modifier = Modifier.height(37.dp))
        TextField(
            labelRes = R.string.loan_amount,
            value = state.loan.amount.toString() + " ₽",
        )
        Spacer(modifier = Modifier.height(6.dp))
        TextField(
            labelRes = R.string.due_date,
//            value = state.loan.endDate.format(formatter).toString(),
            value = "",
        )
        Spacer(modifier = Modifier.height(6.dp))
        TextField(
            labelRes = R.string.interest_rate,
//            value = state.loan.ratePercent.toString() + " %",
            value = ""
        )
        Spacer(modifier = Modifier.height(6.dp))
        TextField(
            labelRes = R.string.debt,
//            value = state.loan.debt.toString() + " ₽",
            value = ""
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}