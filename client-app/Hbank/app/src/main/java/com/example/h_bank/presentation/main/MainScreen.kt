package com.example.h_bank.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import com.example.h_bank.R
import com.example.h_bank.presentation.main.components.AccountsBlock
import com.example.h_bank.presentation.main.components.AccountsBottomSheetContent
import com.example.h_bank.presentation.main.components.ApplicationsBlock
import com.example.h_bank.presentation.main.components.LoansBlock
import com.example.h_bank.presentation.main.components.LoansBottomSheetContent
import com.example.h_bank.presentation.main.components.TransfersBlock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val lazyPagingItems = state.loansFlow.collectAsLazyPagingItems()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is MainNavigationEvent.NavigateToAccount ->
                    navController.navigate("account/${event.accountId}")

                is MainNavigationEvent.NavigateToLoan ->
                    navController.navigate(
                        "loan" +
                                "/${event.loanId}" +
                                "/${event.documentNumber}" +
                                "/${event.amount}" +
                                "/${event.endDate}" +
                                "/${event.ratePercent}" +
                                "/${event.debt}"
                    )

                MainNavigationEvent.NavigateToTransfer ->
                    navController.navigate("transfer")

                MainNavigationEvent.NavigateToReplenishment ->
                    navController.navigate("replenishment")

                MainNavigationEvent.NavigateToWithdrawal ->
                    navController.navigate("withdrawal")

                MainNavigationEvent.NavigateToPaymentHistory ->
                    navController.navigate("payment_history")

                MainNavigationEvent.NavigateToLoanProcessing ->
                    navController.navigate("loan_processing")

                MainNavigationEvent.NavigateToSuccessfulAccountOpening ->
                    navController.navigate("successful_account_opening")

                MainNavigationEvent.NavigateToSuccessfulAccountClosure ->
                    navController.navigate("successful_account_closure")

                MainNavigationEvent.NavigateToWelcome ->
                    navController.navigate("welcome") {
                        popUpTo(
                            "welcome"
                        ) { inclusive = true }
                    }
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
                    IconButton(onClick = { viewModel.onLogoutClicked() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.logout),
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
                        onCloseAccountClick = { viewModel.onCloseAccountClicked(it) },
                        onSeeAllClick = { viewModel.showAccountsSheet() }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
                if (state.initialLoans.isNotEmpty()) {
                    LoansBlock(
                        loans = state.initialLoans,
                        onItemClick = { viewModel.onLoanClicked(it) },
                        onSeeAllClick = { viewModel.showLoansSheet() }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
                TransfersBlock(
                    onTransferClick = { viewModel.onTransferClicked() },
                    onHistoryClick = { viewModel.onHistoryClicked() },
                    onReplenishmentClick = { viewModel.onReplenishmentClicked() },
                    onWithdrawalClick = { viewModel.onWithdrawalClicked() }
                )
                Spacer(modifier = Modifier.height(24.dp))
                ApplicationsBlock(
                    onProcessLoanClick = { viewModel.onProcessLoanClicked() },
                    onOpenAccountClick = { viewModel.onOpenAccountClicked() }
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
                    lazyPagingItems = lazyPagingItems,
                    onItemClick = {
                        viewModel.onLoanClicked(it)
                        viewModel.hideLoansSheet()
                    },
                    onProcessLoanClick = { viewModel.onProcessLoanClicked() }
                )
            }
        }
    }
}
