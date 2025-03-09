package com.example.h_bankpro.presentation.rate

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
import com.example.h_bankpro.presentation.common.TextField
import com.example.h_bankpro.presentation.rate.components.RateHeader
import org.koin.androidx.compose.koinViewModel

@Composable
fun RateScreen(
    navController: NavController,
    viewModel: RateViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is RateNavigationEvent.NavigateToRateEditing ->
                    navController.navigate(
                        "rate_editing" +
                                "/${event.rateId}" +
                                "/${event.name}" +
                                "/${event.interestRate}" +
                                "/${event.description}"
                    )

                RateNavigationEvent.NavigateBack -> navController.popBackStack()
                RateNavigationEvent.NavigateToSuccessfulRateDeletion -> navController.navigate(
                    "successful_rate_deletion"
                )
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
        RateHeader(
            onBackClick = { viewModel.onBackClicked() },
            onDeleteClick = { viewModel.onDeleteClicked() },
            onEditClick = { viewModel.onEditClicked() }
        )
        Spacer(modifier = Modifier.height(37.dp))
        TextField(
            labelRes = R.string.title,
            value = state.name
        )
        Spacer(modifier = Modifier.height(6.dp))
        TextField(
            labelRes = R.string.rate_in_percent,
            value = state.interestRate.toString()
        )
        Spacer(modifier = Modifier.height(6.dp))
        TextField(
            labelRes = R.string.description,
            value = state.description
        )
    }
}