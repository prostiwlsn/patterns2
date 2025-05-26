package com.example.h_bank.presentation.welcome

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.h_bank.R
import com.example.h_bank.domain.repository.authorization.IAuthorizationLocalRepository
import com.example.h_bank.presentation.AuthEventBus
import com.example.h_bank.presentation.AuthTabIntentContract
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun WelcomeScreen(
    navController: NavController,
    viewModel: WelcomeViewModel = koinViewModel(),
    localRepository: IAuthorizationLocalRepository = koinInject()
) {
    val state = viewModel.state.value
    val authLauncher =
        rememberLauncherForActivityResult(contract = AuthTabIntentContract()) { _ -> }

    LaunchedEffect(Unit) {
        AuthEventBus.authEvents.collect { (accessToken, refreshToken) ->
            val isRegistration = AuthEventBus.isRegistration
            if (!accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()) {
                viewModel.handleAuthCallback(accessToken, refreshToken, isRegistration)
            } else {
                println("Invalid auth event: accessToken=$accessToken, refreshToken=$refreshToken")
            }
        }
    }

    LaunchedEffect(key1 = viewModel.navigationEvent) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is WelcomeNavigationEvent.NavigateToLogin -> {
                    AuthEventBus.isRegistration = false
                    val loginUri =
                        Uri.parse("http://194.59.186.122:8080/login?redirect_uri=hbank://auth")
                    authLauncher.launch(loginUri)
                }

                is WelcomeNavigationEvent.NavigateToRegister -> {
                    AuthEventBus.isRegistration = true
                    val registerUri =
                        Uri.parse("http://194.59.186.122:8080/register?redirect_uri=hbank://auth")
                    authLauncher.launch(registerUri)
                }

                is WelcomeNavigationEvent.NavigateToMain -> {
                    navController.navigate("main") {
                        popUpTo("welcome") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    BackHandler(enabled = true) {
        (navController.context as? Activity)?.finish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Image(
                painter = painterResource(id = R.drawable.welcome),
                contentDescription = "Welcome Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Hits bank",
                fontSize = 40.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(R.string.welcome_content),
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFFAEAEB2),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedButton(
                    onClick = { viewModel.onRegister() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFA1A4B2)),
                    enabled = !state.isLoading
                ) {
                    Text(
                        text = stringResource(R.string.registration),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.onLogin() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C49E0)),
                    enabled = !state.isLoading
                ) {
                    Text(
                        text = stringResource(R.string.login),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF5C49E0)
            )
        }
    }
}