package com.example.h_bankpro.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.h_bankpro.presentation.account.AccountScreen
import com.example.h_bankpro.presentation.loan.LoanScreen
import com.example.h_bankpro.presentation.login.LoginScreen
import com.example.h_bankpro.presentation.main.MainScreen
import com.example.h_bankpro.presentation.rate.RateScreen
import com.example.h_bankpro.presentation.rateCreation.RateCreationScreen
import com.example.h_bankpro.presentation.rateEditing.RateEditingScreen
import com.example.h_bankpro.presentation.registration.RegistrationScreen
import com.example.h_bankpro.presentation.successfulRateCreation.SuccessfulRateCreationScreen
import com.example.h_bankpro.presentation.successfulRateEditing.SuccessfulRateEditingScreen
import com.example.h_bankpro.presentation.successfulUserCreation.SuccessfulUserCreationScreen
import com.example.h_bankpro.presentation.transactionInfo.TransactionInfoScreen
import com.example.h_bankpro.presentation.user.UserScreen
import com.example.h_bankpro.presentation.userCreation.UserCreationScreen
import com.example.h_bankpro.presentation.welcome.WelcomeScreen

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
        composable("rate") {
            RateScreen(navController)
        }
        composable("loan") {
            LoanScreen(navController)
        }
        composable(
            route = "user/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType }),
        ) {
            UserScreen(navController)
        }
        composable("user_creation") {
            UserCreationScreen(navController)
        }
        composable("successful_user_creation") {
            SuccessfulUserCreationScreen(navController)
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
        composable("rate_editing") {
            RateEditingScreen(navController)
        }
        composable("account") {
            AccountScreen(navController)
        }
        composable("transaction_info") {
            TransactionInfoScreen(navController)
        }
    }
}