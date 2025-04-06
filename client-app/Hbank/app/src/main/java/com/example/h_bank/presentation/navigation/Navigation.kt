package com.example.h_bank.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.h_bank.presentation.connectionErrorScreen.ConnectionErrorScreen
import com.example.h_bank.presentation.launch.LaunchScreen
import com.example.h_bank.presentation.loan.LoanScreen
import com.example.h_bank.presentation.loanPayment.LoanPaymentScreen
import com.example.h_bank.presentation.loanProcessing.LoanProcessingScreen
import com.example.h_bank.presentation.main.MainScreen
import com.example.h_bank.presentation.paymentHistory.PaymentHistoryScreen
import com.example.h_bank.presentation.replenishment.ReplenishmentScreen
import com.example.h_bank.presentation.successfulAccountClosure.SuccessfulAccountClosureScreen
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
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun AppNavigation(
    viewModel: NavigationViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "launch") {
        composable("launch") {
            LaunchScreen(navController)
        }
        composable("welcome") {
            WelcomeScreen(navController)
        }
        composable("main") {
            MainScreen(navController)
        }
        composable(
            route = "loan/{loanId}/{documentNumber}/{amount}/{endDate}/{ratePercent}/{debt}",
            arguments = listOf(
                navArgument("loanId") { type = NavType.StringType },
                navArgument("documentNumber") { type = NavType.StringType },
                navArgument("amount") { type = NavType.StringType },
                navArgument("endDate") { type = NavType.StringType },
                navArgument("ratePercent") { type = NavType.StringType },
                navArgument("debt") { type = NavType.StringType },
            )
        ) {
            LoanScreen(navController)
        }
        composable(
            route = "loan_payment/{loanId}/{documentNumber}/{debt}",
            arguments = listOf(
                navArgument("loanId") { type = NavType.StringType },
                navArgument("documentNumber") { type = NavType.StringType },
                navArgument("debt") { type = NavType.StringType }
            ),
        ) {
            LoanPaymentScreen(navController)
        }
        composable("loan_processing") {
            LoanProcessingScreen(navController)
        }
        composable("payment_history") {
            PaymentHistoryScreen(navController)
        }
        composable(
            route = "replenishment/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType }),
        ) {
            ReplenishmentScreen(navController)
        }
        composable(
            route = "withdrawal/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType }),
        ) {
            WithdrawalScreen(navController)
        }
        composable(
            route = "transfer/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            TransferScreen(navController)
        }
        composable("successful_account_opening") {
            SuccessfulAccountOpeningScreen(navController)
        }
        composable("successful_account_closure") {
            SuccessfulAccountClosureScreen(navController)
        }
        composable(
            route = "successful_loan_payment/{amount}/{documentNumber}/{debt}",
            arguments = listOf(
                navArgument("amount") { type = NavType.StringType },
                navArgument("documentNumber") { type = NavType.StringType },
                navArgument("debt") { type = NavType.StringType }
            ),
        ) {
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
            route = "successful_transfer/{accountNumber}/{beneficiaryAccountNumber}/{amount}",
            arguments = listOf(
                navArgument("accountNumber") { type = NavType.StringType },
                navArgument("beneficiaryAccountNumber") { type = NavType.StringType },
                navArgument("amount") { type = NavType.StringType }
            ),
        ) {
            SuccessfulTransferScreen(navController)
        }
        composable(
            route = "successful_replenishment/{accountNumber}/{amount}/{currency}",
            arguments = listOf(
                navArgument("accountNumber") { type = NavType.StringType },
                navArgument("amount") { type = NavType.StringType },
                navArgument("currency") { type = NavType.StringType }
            ),
        ) {
            SuccessfulReplenishmentScreen(navController)
        }
        composable(
            route = "successful_withdrawal/{accountNumber}/{amount}/{currency}",
            arguments = listOf(
                navArgument("accountNumber") { type = NavType.StringType },
                navArgument("amount") { type = NavType.StringType },
                navArgument("currency") { type = NavType.StringType }
            ),
        ) {
            SuccessfulWithdrawalScreen(navController)
        }
        composable(
            route = "connectionError"
        ) {
            ConnectionErrorScreen(navController)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                NavigationEvent.NavigateToNoConnection ->
                    if (navController.currentDestination?.route != "connectionError") {
                        navController.navigate("connectionError")
                    }
            }
        }
    }
}