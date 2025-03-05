package com.example.h_bankpro.presentation.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.ITokenStorage
import com.example.h_bankpro.domain.repository.TokenLocalStorage
import com.example.h_bankpro.domain.useCase.RefreshTokenUseCase
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
fun AppNavigation(tokenStorage: ITokenStorage, refreshTokenUseCase: RefreshTokenUseCase) {
    val navController = rememberNavController()
    var isAuthorized by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val currentToken = tokenStorage.getTokenState()
        Log.d("AppNavigation", "Current token: $currentToken")
        if (currentToken.accessToken != null) {
            if (tokenStorage.isTokenValid()) {
                Log.d("AppNavigation", "Token is valid")
                isAuthorized = true
            } else {
                when (val result = refreshTokenUseCase()) {
                    is RequestResult.Success -> {
                        isAuthorized = true
                    }
                    is RequestResult.Error -> {
                        tokenStorage.clearToken()
                        isAuthorized = false
                    }
                    is RequestResult.NoInternetConnection -> {
                        isAuthorized = false
                    }
                }
            }
        } else {
            isAuthorized = false
        }
    }
    NavHost(
        navController = navController,
        startDestination = if (isAuthorized) "main" else "welcome"
    ) {
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
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType }
            ),
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