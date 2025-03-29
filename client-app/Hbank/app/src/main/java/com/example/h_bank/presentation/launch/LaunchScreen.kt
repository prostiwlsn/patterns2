package com.example.h_bank.presentation.launch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
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
import org.koin.androidx.compose.koinViewModel

@Composable
fun LaunchScreen(
    navController: NavController,
    viewModel: LaunchViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    println("LaunchScreen state: $state")

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                LaunchNavigationEvent.NavigateToMain -> {
                    navController.navigate("main") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }

                LaunchNavigationEvent.NavigateToWelcome -> {
                    navController.navigate("welcome") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                color = Color(0xFF5C49E0),
                modifier = Modifier.size(48.dp)
            )
        }
    }
}