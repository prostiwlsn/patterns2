package com.example.h_bankpro.presentation.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.h_bankpro.R
import org.koin.androidx.compose.koinViewModel
import com.example.h_bankpro.presentation.user.components.AccountsBlock
import com.example.h_bankpro.presentation.user.components.AccountsBottomSheetContent
import com.example.h_bankpro.presentation.user.components.LoansBlock
import com.example.h_bankpro.presentation.user.components.LoansBottomSheetContent
import com.example.h_bankpro.presentation.user.components.UserHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    navController: NavController,
    viewModel: UserViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val lazyPagingItems = state.loansFlow.collectAsLazyPagingItems()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is UserNavigationEvent.NavigateToLoan ->
                    navController.navigate(
                        "loan" +
                                "/${event.loanId}" +
                                "/${event.userId}" +
                                "/${event.documentNumber}" +
                                "/${event.amount}" +
                                "/${event.endDate}" +
                                "/${event.ratePercent}" +
                                "/${event.debt}"

                    )

                is UserNavigationEvent.NavigateToAccount ->
                    navController.navigate(
                        "account" +
                                "/${event.accountId}" +
                                "/${event.accountNumber}" +
                                "/${event.currency}"
                    )

                UserNavigationEvent.NavigateBack ->
                    navController.popBackStack()
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
            Spacer(modifier = Modifier.height(40.dp))
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
                UserHeader(
                    name = state.user?.name ?: "",
                    onBackClick = { viewModel.onBackClicked() },
                    onBlockClick = { viewModel.onBlockUserClicked() },
                    onUnblockClick = { viewModel.onUnblockUserClicked() },
                    roles = state.user?.roles ?: emptyList(),
                    isBlocked = state.user?.isBlocked == true
                )
                Spacer(modifier = Modifier.height(24.dp))
                if (state.user?.isBlocked == true) {
                    Text(
                        text = stringResource(R.string.blocked),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                if (state.accounts.isNotEmpty()) {
                    AccountsBlock(
                        accounts = state.accounts,
                        onItemClick = { viewModel.onAccountClicked(it) },
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
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (state.creditRating != null) {
                Text(
                    text = "Social Credit: ${state.creditRating}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
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
                    onItemClick = {
                        viewModel.onAccountClicked(it)
                        viewModel.hideAccountsSheet()
                    }
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
                    }
                )
            }
        }
    }
}
