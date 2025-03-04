package com.example.h_bankpro.presentation.userCreation

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
import com.example.h_bankpro.presentation.common.CustomDisablableButton
import com.example.h_bankpro.presentation.common.TextInputField
import com.example.h_bankpro.presentation.userCreation.components.RoleToggleSwitch
import com.example.h_bankpro.presentation.userCreation.components.UserCreationHeader
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserCreationScreen(
    navController: NavController,
    viewModel: UserCreationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is UserCreationNavigationEvent.NavigateToSuccessfulUserCreation ->
                    navController.navigate("successful_user_creation")

                UserCreationNavigationEvent.NavigateBack -> navController.popBackStack()
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
        UserCreationHeader(
            onBackClick = { viewModel.onBackClicked() }
        )
        Spacer(modifier = Modifier.height(37.dp))
        TextInputField(
            labelRes = R.string.name,
            value = state.name,
            onValueChange = { viewModel.onNameChange(it) }
        )
        Spacer(modifier = Modifier.height(6.dp))
        TextInputField(
            labelRes = R.string.phone,
            value = state.phone,
            onValueChange = { viewModel.onPhoneChange(it) }
        )
        Spacer(modifier = Modifier.height(6.dp))
        TextInputField(
            labelRes = R.string.password,
            value = state.password,
            onValueChange = { viewModel.onPasswordChange(it) }
        )
        Spacer(modifier = Modifier.height(6.dp))
        TextInputField(
            labelRes = R.string.repeat_password,
            value = state.repeatPassword,
            onValueChange = { viewModel.onRepeatPasswordChange(it) }
        )
        Spacer(modifier = Modifier.height(12.dp))
        RoleToggleSwitch(
            isClientSelected = state.selectedRole,
            onRoleSelected = { viewModel.onRoleSelect(it) }
        )
        Spacer(modifier = Modifier.weight(1f))
        CustomDisablableButton(
            onClick = viewModel::onCreateClicked,
            textRes = R.string.create,
            enabled = state.areFieldsValid
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}