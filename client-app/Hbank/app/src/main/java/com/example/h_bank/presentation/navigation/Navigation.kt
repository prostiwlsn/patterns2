package com.example.h_bank.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.example.h_bank.presentation.login.LoginScreen
import com.example.h_bank.presentation.main.MainScreen
import com.example.h_bank.presentation.registration.RegistrationScreen
import com.example.h_bank.presentation.successfulAccountOpening.SuccessfulAccountOpeningScreen
import com.example.h_bank.presentation.successfulLoanProcessing.SuccessfulLoanProcessingScreen
import com.example.h_bank.presentation.welcome.WelcomeScreen

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
        composable("successful_account_opening") {
            SuccessfulAccountOpeningScreen(navController)
        }
        composable("successful_loan_processing") {
            SuccessfulLoanProcessingScreen(navController)
        }
    }
}