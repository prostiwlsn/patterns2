package com.example.h_bankpro.presentation.main

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
import com.example.h_bankpro.R
import com.example.h_bankpro.presentation.main.components.UsersBottomSheetContent
import com.example.h_bankpro.presentation.main.components.ActionBlock
import com.example.h_bankpro.presentation.main.components.RatesBlock
import com.example.h_bankpro.presentation.main.components.RatesBottomSheetContent
import com.example.h_bankpro.presentation.main.components.UsersBlock

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
                is MainNavigationEvent.NavigateToUser ->
                    navController.navigate("user/${event.userId}")

                is MainNavigationEvent.NavigateToRateCreation ->
                    navController.navigate("rate_creation")

                is MainNavigationEvent.NavigateToUserCreation ->
                    navController.navigate("user_creation")

                is MainNavigationEvent.NavigateToRate ->
                    navController.navigate("rate")
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
            if (state.users.isNotEmpty()) {
                UsersBlock(
                    users = state.users,
                    onItemClick = { viewModel.onUserClicked(it) },
                    onSeeAllClick = { viewModel.showUsersSheet() }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            ActionBlock(
                onCreateRateClick = { viewModel.onCreateRateClicked() },
                onCreateUserClick = { viewModel.onCreateUserClicked() }
            )
            Spacer(modifier = Modifier.height(24.dp))
            if (state.rates.isNotEmpty()) {
                RatesBlock(
                    rates = state.rates,
                    onItemClick = { viewModel.onRateClicked(it) },
                    onSeeAllClick = { viewModel.showRatesSheet() }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
        if (state.isUsersSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.hideUsersSheet() },
                containerColor = Color(0xFFF9F9F9),
                shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
            ) {
                UsersBottomSheetContent(
                    users = state.users,
                    onItemClick = { account ->
                        viewModel.onUserClicked(account)
                        viewModel.hideUsersSheet()
                    }
                )
            }
        }
        if (state.isRatesSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.hideRatesSheet() },
                containerColor = Color(0xFFF9F9F9),
                shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
            ) {
                RatesBottomSheetContent(
                    rates = state.rates,
                    onItemClick = { rate ->
                        viewModel.onRateClicked(rate)
                        viewModel.hideRatesSheet()
                    }
                )
            }
        }
    }
}
