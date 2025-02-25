package com.example.h_bank.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import com.example.h_bank.R
import com.example.h_bank.presentation.main.components.AccountsBlock
import com.example.h_bank.presentation.main.components.ApplicationsBlock
import com.example.h_bank.presentation.main.components.LoansBlock
import com.example.h_bank.presentation.main.components.TransfersBlock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is MainNavigationEvent.NavigateToAccount ->
                    navController.navigate("account/${event.accountId}")

                is MainNavigationEvent.NavigateToLoan ->
                    navController.navigate("credit/${event.loanId}")

                is MainNavigationEvent.NavigateToTransfer ->
                    navController.navigate("transfer")

                is MainNavigationEvent.NavigateToHistory ->
                    navController.navigate("history")

                is MainNavigationEvent.NavigateToLoanProcessing ->
                    navController.navigate("loan_processing")

                is MainNavigationEvent.NavigateToSuccessfulAccountOpening ->
                    navController.navigate("successful_account_opening")
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(36.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.good_day),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.05.sp,
                    color = Color.Black
                )
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            if (state.accounts.isNotEmpty()) {
                AccountsBlock(
                    accounts = state.accounts,
                    onItemClick = { viewModel.onAccountClicked(it) },
                    onSeeAllClick = { viewModel.showAccountsSheet() }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            if (state.loans.isNotEmpty()) {
                LoansBlock(
                    loans = state.loans,
                    onItemClick = { viewModel.onLoanClicked(it) },
                    onSeeAllClick = { viewModel.showLoansSheet() }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            TransfersBlock(
                onTransferClick = { viewModel.onTransferClicked() },
                onHistoryClick = { viewModel.onHistoryClicked() }
            )
            Spacer(modifier = Modifier.height(24.dp))
            ApplicationsBlock(
                onProcessLoanClick = { viewModel.onProcessLoanClicked() },
                onOpenAccountClick = { viewModel.onOpenAccountClicked() }
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
        if (state.isAccountsSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.hideAccountsSheet() },
                containerColor = Color(0xFFF9F9F9),
                shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
            ) {
                AccountsBottomSheetContent(
                    accounts = state.accounts,
                    onItemClick = { account ->
                        viewModel.onAccountClicked(account)
                        viewModel.hideAccountsSheet()
                    },
                    onOpenAccountClick = { }
                )
            }
        }
        if (state.isLoansSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.hideLoansSheet() },
                containerColor = Color(0xFFF9F9F9),
                shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
            ) {
                LoansBottomSheetContent(
                    loans = state.loans,
                    onItemClick = { loan ->
                        viewModel.onLoanClicked(loan)
                        viewModel.hideLoansSheet()
                    },
                    onProcessLoanClick = { viewModel.onProcessLoanClicked() }
                )
            }
        }
    }
}
