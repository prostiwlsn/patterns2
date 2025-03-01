package com.example.h_bank.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.h_bank.presentation.loan.LoanScreen
import com.example.h_bank.presentation.loanPayment.LoanPaymentScreen
import com.example.h_bank.presentation.loanProcessing.LoanProcessingScreen
import com.example.h_bank.presentation.login.LoginScreen
import com.example.h_bank.presentation.main.MainScreen
import com.example.h_bank.presentation.paymentHistory.PaymentHistoryScreen
import com.example.h_bank.presentation.registration.RegistrationScreen
import com.example.h_bank.presentation.successfulAccountOpening.SuccessfulAccountOpeningScreen
import com.example.h_bank.presentation.successfulLoanPayment.SuccessfulLoanPaymentScreen
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
        composable(
            route = "loan/{loanId}",
            arguments = listOf(navArgument("loanId") { type = NavType.StringType }),
        ) {
            LoanScreen(navController)
        }
        composable(
            route = "loan_payment/{loanId}",
            arguments = listOf(navArgument("loanId") { type = NavType.StringType }),
        ) {
            LoanPaymentScreen(navController)
        }
        composable("loan_processing") {
            LoanProcessingScreen(navController)
        }
        composable("payment_history") {
            PaymentHistoryScreen(navController)
        }
        composable("successful_account_opening") {
            SuccessfulAccountOpeningScreen(navController)
        }
        composable("successful_loan_payment") {
            SuccessfulLoanPaymentScreen(navController)
        }
        composable("successful_loan_processing") {
            SuccessfulLoanProcessingScreen(navController)
        }
    }
}