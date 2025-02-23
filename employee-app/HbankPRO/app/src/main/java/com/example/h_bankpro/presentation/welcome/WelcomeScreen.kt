package com.example.h_bankpro.presentation.welcome

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import org.koin.androidx.compose.koinViewModel
import com.example.h_bankpro.R

@Composable
fun WelcomeScreen(
    navController: NavController,
    viewModel: WelcomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                WelcomeNavigationEvent.NavigateToLogin -> navController.navigate("login")
                WelcomeNavigationEvent.NavigateToRegister -> navController.navigate("registration")
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
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
            text = stringResource(R.string.hits_bank),
            fontSize = 40.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            color = Color.Black,
            lineHeight = 50.sp
        )
        Spacer(modifier = Modifier.height(40.dp))
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
                onClick = {viewModel.onRegister()},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFFA1A4B2))
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
                onClick = {viewModel.onLogin()},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C49E0))
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
}
