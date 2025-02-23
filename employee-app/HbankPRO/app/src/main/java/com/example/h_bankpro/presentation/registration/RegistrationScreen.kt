package com.example.h_bank.presentation.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.h_bankpro.R
import com.example.h_bankpro.presentation.registration.RegistrationNavigationEvent
import com.example.h_bankpro.presentation.registration.RegistrationViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    navController: NavController,
    viewModel: RegistrationViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                RegistrationNavigationEvent.NavigateToWelcome -> navController.navigate("home")
                RegistrationNavigationEvent.NavigateToLogin -> navController.navigate("login")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Spacer(modifier = Modifier.width(32.dp))
            Text(
                text = stringResource(R.string.registration),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        OutlinedTextField(
            value = state.value.name,
            onValueChange = { viewModel.onNameChange(it) },
            label = { Text(stringResource(R.string.name)) },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF5C49E0),
                unfocusedBorderColor = Color(0xFFBCBFCD)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = state.value.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text(stringResource(R.string.email)) },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF5C49E0),
                unfocusedBorderColor = Color(0xFFBCBFCD)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = state.value.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text(stringResource(R.string.password)) },
            shape = RoundedCornerShape(8.dp),
            visualTransformation = if (state.value.isPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { viewModel.onChangePasswordVisibility() }) {
                    Icon(
                        imageVector = if (state.value.isPasswordVisible) ImageVector.vectorResource(
                            R.drawable.visibility_on
                        ) else ImageVector.vectorResource(R.drawable.visibility_off),
                        contentDescription = "Toggle Password Visibility"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF5C49E0),
                unfocusedBorderColor = Color(0xFFBCBFCD)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = state.value.repeatPassword,
            onValueChange = { viewModel.onRepeatPasswordChange(it) },
            label = { Text(stringResource(R.string.repeat_password)) },
            shape = RoundedCornerShape(8.dp),
            visualTransformation = if (state.value.isRepeatPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { viewModel.onChangeRepeatPasswordVisibility() }) {
                    Icon(
                        imageVector = if (state.value.isRepeatPasswordVisible) ImageVector.vectorResource(
                            R.drawable.visibility_on
                        ) else ImageVector.vectorResource(R.drawable.visibility_off),
                        contentDescription = "Toggle Password Visibility"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF5C49E0),
                unfocusedBorderColor = Color(0xFFBCBFCD)
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { viewModel.onRegister() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5C49E0),
                disabledContainerColor = Color(0xFFA396FF)
            ),
            enabled = state.value.areFieldsValid
        ) {
            Text(
                text = stringResource(R.string.registration_button),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.registration_bottom_text),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFFBCBFCD)
            )
            Text(
                text = stringResource(R.string.registration_bottom_text_2),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5C49E0),
                modifier = Modifier.clickable {
                    viewModel.onLogin()
                }
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}