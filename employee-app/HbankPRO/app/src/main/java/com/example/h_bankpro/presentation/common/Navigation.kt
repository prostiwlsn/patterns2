package com.example.h_bankpro.presentation.common

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.example.h_bankpro.presentation.login.LoginScreen
import com.example.h_bank.presentation.registration.RegistrationScreen
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
    }
}