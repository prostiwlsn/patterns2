package com.example.h_bankpro.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.h_bankpro.R
import com.example.h_bankpro.presentation.common.CustomDisablableButton
import com.example.h_bankpro.presentation.login.components.LoginBottomText
import com.example.h_bankpro.presentation.login.components.LoginForm
import com.example.h_bankpro.presentation.login.components.LoginHeader
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                LoginNavigationEvent.NavigateToRegister -> navController.navigate("registration")
                LoginNavigationEvent.NavigateToMain -> navController.navigate("main") {
                    popUpTo("welcome") { inclusive = true }
                    launchSingleTop = true
                }

                LoginNavigationEvent.NavigateBack -> {
                    val canNavigateBack = navController.previousBackStackEntry != null
                    if (canNavigateBack) {
                        navController.popBackStack()
                    }
                }
            }
        }
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
        LoginHeader(onBackClick = { viewModel.onBackClicked() })
        Spacer(modifier = Modifier.weight(1f))
        LoginForm(
            state = state.value,
            onLoginChange = viewModel::onLoginChange,
            onPasswordChange = viewModel::onPasswordChange,
            onChangePasswordVisibility = viewModel::onChangePasswordVisibility,
        )
        Spacer(modifier = Modifier.weight(1f))
        CustomDisablableButton(
            enabled = state.value.areFieldsValid,
            onClick = viewModel::onLoginClicked,
            textRes = R.string.login
        )
        Spacer(modifier = Modifier.height(16.dp))
        LoginBottomText(onRegisterClick = viewModel::onRegisterClicked)
        Spacer(modifier = Modifier.height(32.dp))
    }
}