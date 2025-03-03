package com.example.h_bankpro.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.h_bankpro.presentation.login.LoginScreen
import com.example.h_bankpro.presentation.main.MainScreen
import com.example.h_bankpro.presentation.rateCreation.RateCreationScreen
import com.example.h_bankpro.presentation.rateEditing.RateEditingScreen
import com.example.h_bankpro.presentation.registration.RegistrationScreen
import com.example.h_bankpro.presentation.successfulRateCreation.SuccessfulRateCreationScreen
import com.example.h_bankpro.presentation.successfulRateEditing.SuccessfulRateEditingScreen
import com.example.h_bankpro.presentation.welcome.WelcomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(navController)
        }
        composable("registration") {
            RegistrationScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("main") {
            MainScreen(navController)
        }
        composable("rate_creation") {
            RateCreationScreen(navController)
        }
        composable("successful_rate_creation") {
            SuccessfulRateCreationScreen(navController)
        }
        composable("successful_rate_editing") {
            SuccessfulRateEditingScreen(navController)
        }
        composable(
            route = "rate_editing/{rateId}",
            arguments = listOf(navArgument("rateId") { type = NavType.StringType }),
        ) {
            RateEditingScreen(navController)
        }
    }
}