package com.example.h_bankpro.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.h_bankpro.presentation.account.AccountScreen
import com.example.h_bankpro.presentation.connectionError.ConnectionErrorScreen
import com.example.h_bankpro.presentation.launch.LaunchScreen
import com.example.h_bankpro.presentation.loan.LoanScreen
import com.example.h_bankpro.presentation.main.MainScreen
import com.example.h_bankpro.presentation.rate.RateScreen
import com.example.h_bankpro.presentation.rateCreation.RateCreationScreen
import com.example.h_bankpro.presentation.rateEditing.RateEditingScreen
import com.example.h_bankpro.presentation.successfulRateCreation.SuccessfulRateCreationScreen
import com.example.h_bankpro.presentation.successfulRateEditing.SuccessfulRateEditingScreen
import com.example.h_bankpro.presentation.successfulUserCreation.SuccessfulUserCreationScreen
import com.example.h_bankpro.presentation.operationInfo.OperationInfoScreen
import com.example.h_bankpro.presentation.serverError.ServerErrorScreen
import com.example.h_bankpro.presentation.successfulRateDeletion.SuccessfulRateDeletionScreen
import com.example.h_bankpro.presentation.user.UserScreen
import com.example.h_bankpro.presentation.userCreation.UserCreationScreen
import com.example.h_bankpro.presentation.welcome.WelcomeScreen
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun AppNavigation(
    viewModel: NavigationViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "launch"
    ) {
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
            route = "rate/{rateId}/{name}/{interestRate}/{description}",
            arguments = listOf(
                navArgument("rateId") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType },
                navArgument("interestRate") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
            )
        ) {
            RateScreen(navController)
        }
        composable(
            route = "loan/{loanId}/{userId}/{documentNumber}/{amount}/{endDate}/{ratePercent}/{debt}",
            arguments = listOf(
                navArgument("loanId") { type = NavType.StringType },
                navArgument("userId") { type = NavType.StringType },
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
            route = "user/{userId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType }
            )
        ) {
            UserScreen(navController)
        }
        composable("user_creation") {
            UserCreationScreen(navController)
        }
        composable("successful_user_creation") {
            SuccessfulUserCreationScreen(navController)
        }
        composable("successful_rate_deletion") {
            SuccessfulRateDeletionScreen(navController)
        }
        composable("rate_creation") {
            RateCreationScreen(navController)
        }
        composable("successful_rate_creation") {
            SuccessfulRateCreationScreen(navController)
        }
        composable(
            "successful_rate_editing"
        ) {
            SuccessfulRateEditingScreen(navController)
        }
        composable(
            route = "rate_editing/{rateId}/{name}/{interestRate}/{description}",
            arguments = listOf(
                navArgument("rateId") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType },
                navArgument("interestRate") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
            )
        ) {
            RateEditingScreen(navController)
        }
        composable(
            route = "account/{accountId}/{accountNumber}/{currency}",
            arguments = listOf(
                navArgument("accountId") { type = NavType.StringType },
                navArgument("accountNumber") { type = NavType.StringType },
                navArgument("currency") { type = NavType.StringType }
            )
        ) {
            AccountScreen(navController)
        }
        composable(
            route = "operation_info/{accountId}/{operationId}",
            arguments = listOf(
                navArgument("accountId") { type = NavType.StringType },
                navArgument("operationId") { type = NavType.StringType }
            )
        ) {
            OperationInfoScreen(navController)
        }
        composable(
            route = "connectionError"
        ) {
            ConnectionErrorScreen(navController)
        }
        composable(
            route = "serverError"
        ) {
            ServerErrorScreen(navController)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                NavigationEvent.NavigateToNoConnection ->
                    if (navController.currentDestination?.route != "connectionError") {
                        navController.navigate("connectionError")
                    }

                NavigationEvent.NavigateToServerError ->
                    if (navController.currentDestination?.route != "serverError") {
                        navController.navigate("serverError")
                    }
            }
        }
    }
}