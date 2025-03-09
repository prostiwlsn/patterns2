package com.example.h_bankpro.presentation.connectionError

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.h_bankpro.R
import com.example.h_bankpro.presentation.common.CustomButton
import com.example.h_bankpro.presentation.common.ErrorIcon
import com.example.h_bankpro.presentation.common.SuccessMessage
import org.koin.androidx.compose.koinViewModel


@Composable
fun ConnectionErrorScreen(
    navController: NavController,
    viewModel: ConnectionErrorViewModel = koinViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                ConnectionErrorNavigationEvent.NavigateBack ->
                    navController.popBackStack()
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
            .background(Color.White),
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
                    contentDescription = "Back"
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        ErrorIcon()
        Spacer(modifier = Modifier.height(16.dp))
        SuccessMessage(textRes = R.string.no_connection_message)
        Spacer(modifier = Modifier.weight(1f))
        CustomButton(
            onClick = viewModel::onToMainClicked,
            textRes = R.string.go_back
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}