package com.example.h_bank.presentation.successfulLoanProcessing

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.h_bank.R
import com.example.h_bank.presentation.common.CustomButton
import com.example.h_bank.presentation.common.SuccessIcon
import com.example.h_bank.presentation.common.SuccessMessage
import org.koin.androidx.compose.koinViewModel

@Composable
fun SuccessfulLoanProcessingScreen(
    navController: NavController,
    viewModel: SuccessfulLoanProcessingViewModel = koinViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                SuccessfulLoanProcessingNavigationEvent.NavigateToMain ->
                    navController.navigate("main") {
                        popUpTo(
                            "main"
                        ) { inclusive = true }
                    }
            }
        }
    }

    BackHandler {
        viewModel.onToMainClicked()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(onClick = { viewModel.onToMainClicked() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        SuccessIcon()
        Spacer(modifier = Modifier.height(16.dp))
        SuccessMessage(textRes = R.string.success_loan)
        Spacer(modifier = Modifier.weight(1f))
        CustomButton(
            onClick = viewModel::onToMainClicked,
            textRes = R.string.to_main
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}