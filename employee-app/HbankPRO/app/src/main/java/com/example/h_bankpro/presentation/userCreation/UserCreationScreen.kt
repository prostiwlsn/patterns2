package com.example.h_bankpro.presentation.userCreation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import com.example.h_bankpro.R
import com.example.h_bankpro.presentation.common.CustomDisablableButton
import com.example.h_bankpro.presentation.common.TextInputField
import com.example.h_bankpro.presentation.userCreation.components.RoleToggleSwitch
import com.example.h_bankpro.presentation.userCreation.components.UserCreationHeader
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = state.phone,
            onValueChange = { newValue ->
                if (newValue.isDigitsOnly() && newValue.length <= 10) {
                    viewModel.onPhoneChange(newValue)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            prefix = { Text(stringResource(id = R.string.phone_number_prefix)) },
            label = { Text(stringResource(R.string.phone)) },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF5C49E0),
                unfocusedBorderColor = Color(0xFFBCBFCD)
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        TextInputField(
            labelRes = R.string.password,
            value = state.password,
            onValueChange = { viewModel.onPasswordChange(it) }
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