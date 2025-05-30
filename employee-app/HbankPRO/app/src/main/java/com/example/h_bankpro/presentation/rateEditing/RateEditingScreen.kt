package com.example.h_bankpro.presentation.rateEditing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import com.example.h_bankpro.presentation.common.FloatInputField
import com.example.h_bankpro.presentation.common.TextInputField
import com.example.h_bankpro.presentation.rateEditing.components.RateEditingHeader
import org.koin.androidx.compose.koinViewModel

@Composable
fun RateEditingScreen(
    navController: NavController,
    viewModel: RateEditingViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                RateEditingNavigationEvent.NavigateToSuccessfulRateEditing ->
                    navController.navigate("successful_rate_editing")

                RateEditingNavigationEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        RateEditingHeader(
            onBackClick = { viewModel.onBackClicked() }
        )
        Spacer(modifier = Modifier.height(37.dp))
        TextInputField(
            labelRes = R.string.title,
            value = state.name,
            onValueChange = { viewModel.onNameChange(it) }
        )
        Spacer(modifier = Modifier.height(6.dp))
        FloatInputField(
            labelRes = R.string.rate_in_percent,
            value = state.interestRate.orEmpty(),
            onValueChange = {
                viewModel.onRateChange(it)
            }
        )
        Spacer(modifier = Modifier.height(6.dp))
        TextInputField(
            labelRes = R.string.description,
            value = state.description,
            onValueChange = { viewModel.onDescriptionChange(it) }
        )
        Spacer(modifier = Modifier.weight(1f))
        CustomDisablableButton(
            onClick = viewModel::onSaveClicked,
            textRes = R.string.save,
            enabled = state.areFieldsValid
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}