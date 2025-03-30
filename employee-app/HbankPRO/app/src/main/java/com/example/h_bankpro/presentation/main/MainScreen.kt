package com.example.h_bankpro.presentation.main

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import org.koin.androidx.compose.koinViewModel
import com.example.h_bankpro.R
import com.example.h_bankpro.data.ThemeMode
import com.example.h_bankpro.presentation.main.components.UsersBottomSheetContent
import com.example.h_bankpro.presentation.main.components.ActionBlock
import com.example.h_bankpro.presentation.main.components.TariffsBlock
import com.example.h_bankpro.presentation.main.components.TariffsBottomSheetContent
import com.example.h_bankpro.presentation.main.components.UsersBlock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val lazyPagingItems = state.tariffsFlow.collectAsLazyPagingItems()

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
                    navController.navigate(
                        "rate" +
                                "/${event.rateId}" +
                                "/${event.name}" +
                                "/${event.interestRate}" +
                                "/${event.description}"
                    )

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
                if (state.initialTariffs.isNotEmpty()) {
                    TariffsBlock(
                        tariffs = state.initialTariffs,
                        onItemClick = { viewModel.onTariffClicked(it) },
                        onSeeAllClick = { viewModel.showRatesSheet() }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
        if (state.isUsersSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.hideUsersSheet() },
                containerColor = MaterialTheme.colorScheme.surface,
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
        if (state.isTariffsSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.hideRatesSheet() },
                containerColor = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
            ) {
                TariffsBottomSheetContent(
                    lazyPagingItems = lazyPagingItems,
                    onItemClick = { tariff ->
                        viewModel.onTariffClicked(tariff)
                        viewModel.hideRatesSheet()
                    }
                )
            }
        }
    }
}
