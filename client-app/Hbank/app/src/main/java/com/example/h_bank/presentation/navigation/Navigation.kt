package com.example.h_bank.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.h_bank.presentation.replenishment.ReplenishmentScreen
import com.example.h_bank.presentation.successfulAccountOpening.SuccessfulAccountOpeningScreen
import com.example.h_bank.presentation.successfulLoanPayment.SuccessfulLoanPaymentScreen
import com.example.h_bank.presentation.successfulLoanProcessing.SuccessfulLoanProcessingScreen
import com.example.h_bank.presentation.successfulReplenishment.SuccessfulReplenishmentScreen
import com.example.h_bank.presentation.successfulTransfer.SuccessfulTransferScreen
import com.example.h_bank.presentation.successfulWithdrawal.SuccessfulWithdrawalScreen
import com.example.h_bank.presentation.transactionInfo.TransactionInfoScreen
import com.example.h_bank.presentation.transfer.TransferScreen
import com.example.h_bank.presentation.welcome.WelcomeScreen
import com.example.h_bank.presentation.withdrawal.WithdrawalScreen

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
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
        composable("replenishment") {
            ReplenishmentScreen(navController)
        }
        composable("withdrawal") {
            WithdrawalScreen(navController)
        }
        composable("transfer") {
            TransferScreen(navController)
        }
        composable("successful_account_opening") {
            SuccessfulAccountOpeningScreen(navController)
        }
        composable("successful_loan_payment") {
            SuccessfulLoanPaymentScreen(navController)
        }
        composable(
            route = "transaction_info/{transactionId}",
            arguments = listOf(
                navArgument("transactionId") { type = NavType.StringType }
            ),
        ) {
            TransactionInfoScreen(navController)
        }
        composable("successful_loan_processing") {
            SuccessfulLoanProcessingScreen(navController)
        }
        composable(
            route = "successful_transfer/{accountId}/{beneficiaryAccountId}/{amount}",
            arguments = listOf(
                navArgument("accountId") { type = NavType.StringType },
                navArgument("beneficiaryAccountId") { type = NavType.StringType },
                navArgument("amount") { type = NavType.LongType }
            ),
        ) {
            SuccessfulTransferScreen(navController)
        }
        composable(
            route = "successful_replenishment/{accountId}/{amount}",
            arguments = listOf(
                navArgument("accountId") { type = NavType.StringType },
                navArgument("amount") { type = NavType.LongType }
            ),
        ) {
            SuccessfulReplenishmentScreen(navController)
        }
        composable(
            route = "successful_withdrawal/{accountId}/{amount}",
            arguments = listOf(
                navArgument("accountId") { type = NavType.StringType },
                navArgument("amount") { type = NavType.LongType }
            ),
        ) {
            SuccessfulWithdrawalScreen(navController)
        }
    }
}