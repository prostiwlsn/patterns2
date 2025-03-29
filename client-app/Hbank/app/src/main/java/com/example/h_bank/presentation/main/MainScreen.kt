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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import com.example.h_bank.R
import com.example.h_bank.data.ThemeMode
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

                is MainNavigationEvent.NavigateToTransfer ->
                    navController.navigate("transfer/${event.userId}")

                is MainNavigationEvent.NavigateToReplenishment ->
                    navController.navigate("replenishment/${event.userId}")

                is MainNavigationEvent.NavigateToWithdrawal ->
                    navController.navigate("withdrawal/${event.userId}")

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
            .background(MaterialTheme.colorScheme.background)
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
                        color = MaterialTheme.colorScheme.primary
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
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Row {
                        IconButton(onClick = {
                            viewModel.toggleTheme()
                        }
                        ) {
                            Icon(
                                painter = painterResource(
                                    if (state.themeMode == ThemeMode.LIGHT) R.drawable.moon else R.drawable.sun
                                ),
                                contentDescription = "Toggle theme",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        IconButton(onClick = { viewModel.onLogoutClicked() }) {
                            Icon(
                                painter = painterResource(R.drawable.logout),
                                contentDescription = "Logout",
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                if (state.accounts.isNotEmpty()) {
                    AccountsBlock(
                        accounts = state.accounts.filter { !state.hiddenAccounts.contains(it.id) },
                        hiddenAccounts = state.hiddenAccounts,
                        onToggleVisibility = { viewModel.toggleAccountVisibility(it) },
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
                containerColor = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
            ) {
                AccountsBottomSheetContent(
                    accounts = state.accounts,
                    hiddenAccounts = state.hiddenAccounts,
                    onToggleVisibility = { viewModel.toggleAccountVisibility(it) },
                    onCloseAccountClick = { account ->
                        viewModel.onCloseAccountClicked(account)
                        viewModel.hideAccountsSheet()
                    },
                    onOpenAccountClick = { viewModel.onOpenAccountClicked() }
                )
            }
        }
        if (state.isLoansSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.hideLoansSheet() },
                containerColor = MaterialTheme.colorScheme.surface,
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
