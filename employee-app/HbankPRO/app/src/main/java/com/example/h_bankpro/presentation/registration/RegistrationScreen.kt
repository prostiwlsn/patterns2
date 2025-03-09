package com.example.h_bankpro.presentation.registration

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.h_bankpro.R
import com.example.h_bankpro.presentation.common.CustomDisablableButton
import com.example.h_bankpro.presentation.registration.components.RegistrationBottomText
import com.example.h_bankpro.presentation.registration.components.RegistrationForm
import com.example.h_bankpro.presentation.registration.components.RegistrationHeader
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegistrationScreen(
    navController: NavController,
    viewModel: RegistrationViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                RegistrationNavigationEvent.NavigateToLogin -> navController.navigate("login") {
                    popUpTo("welcome") { inclusive = false }
                    launchSingleTop = true
                }
                RegistrationNavigationEvent.NavigateToMain -> navController.navigate("main") {
                    popUpTo("welcome") { inclusive = true }
                    launchSingleTop = true
                }

                RegistrationNavigationEvent.NavigateBack -> {
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
        RegistrationHeader(onBackClick = { viewModel.onBackClicked() })
        Spacer(modifier = Modifier.weight(1f))
        RegistrationForm(
            state = state.value,
            onNameChange = viewModel::onNameChange,
            onEmailChange = viewModel::onPhoneNumberChange,
            onPasswordChange = viewModel::onPasswordChange,
            onRepeatPasswordChange = viewModel::onRepeatPasswordChange,
            onChangePasswordVisibility = viewModel::onChangePasswordVisibility,
            onChangeRepeatPasswordVisibility = viewModel::onChangeRepeatPasswordVisibility
        )
        Spacer(modifier = Modifier.weight(1f))
        CustomDisablableButton(
            enabled = state.value.areFieldsValid,
            onClick = viewModel::onRegisterClicked,
            textRes = R.string.registration_button
        )
        Spacer(modifier = Modifier.height(16.dp))
        RegistrationBottomText(onLoginClick = viewModel::onLoginClicked)
        Spacer(modifier = Modifier.height(32.dp))
    }
}