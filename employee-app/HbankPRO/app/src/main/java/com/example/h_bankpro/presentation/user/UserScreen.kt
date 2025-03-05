package com.example.h_bankpro.presentation.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                UserNavigationEvent.NavigateToLoan ->
                    navController.navigate("loan")

                UserNavigationEvent.NavigateToAccount ->
                    navController.navigate("account")

                UserNavigationEvent.NavigateBack ->
                    navController.popBackStack()
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
            Spacer(modifier = Modifier.height(40.dp))
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
                    color = Color.Red
                )
            }
            if (state.accounts.isNotEmpty()) {
                AccountsBlock(
                    accounts = state.accounts,
                    onItemClick = { viewModel.onAccountClicked() },
                    onSeeAllClick = { viewModel.showAccountsSheet() }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            if (state.loans.isNotEmpty()) {
                LoansBlock(
                    loans = state.loans,
                    onItemClick = { viewModel.onLoanClicked() },
                    onSeeAllClick = { viewModel.showLoansSheet() }
                )
            }
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
                    onItemClick = {
                        viewModel.onAccountClicked()
                        viewModel.hideAccountsSheet()
                    }
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
                    onItemClick = {
                        viewModel.onLoanClicked()
                        viewModel.hideLoansSheet()
                    }
                )
            }
        }
    }
}
