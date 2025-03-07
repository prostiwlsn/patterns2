package com.example.h_bank.presentation.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.h_bank.R
import com.example.h_bank.presentation.login.LoginState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginForm(
    state: LoginState,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onChangePasswordVisibility: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = state.login,
            onValueChange = { newValue ->
                if (newValue.isDigitsOnly() && newValue.length <= 10) {
                    onLoginChange(newValue)
                }
            },
            isError = state.fieldErorrs?.loginFieldError != null,
            supportingText = {
                state.fieldErorrs?.loginFieldError?.let {
                    Text(text = it)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            prefix = { Text(stringResource(id = R.string.phone_number_prefix)) },
            label = { Text(stringResource(R.string.user_login)) },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF5C49E0),
                unfocusedBorderColor = Color(0xFFBCBFCD)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = state.password,
            onValueChange = { newValue ->
                if (newValue.length <= 32) {
                    onPasswordChange(newValue)
                }
            },
            isError = state.fieldErorrs?.passwordFieldError != null,
            supportingText = {
                state.fieldErorrs?.passwordFieldError?.let {
                    Text(text = it)
                }
            },
            label = { Text(stringResource(R.string.password)) },
            shape = RoundedCornerShape(8.dp),
            visualTransformation = if (state.isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = onChangePasswordVisibility) {
                    Icon(
                        imageVector = if (state.isPasswordVisible)
                            ImageVector.vectorResource(id = R.drawable.visibility_on)
                        else
                            ImageVector.vectorResource(id = R.drawable.visibility_off),
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
    }
}
