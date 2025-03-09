package com.example.h_bankpro.presentation.registration.components

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
import com.example.h_bankpro.R
import com.example.h_bankpro.presentation.registration.RegistrationState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationForm(
    state: RegistrationState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    onChangePasswordVisibility: () -> Unit,
    onChangeRepeatPasswordVisibility: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = state.name,
            onValueChange = onNameChange,
            label = { Text(stringResource(R.string.name)) },
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            isError = state.fieldErrors?.nameFieldError != null,
            supportingText = {
                state.fieldErrors?.nameFieldError?.let {
                    Text(text = it)
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF5C49E0),
                unfocusedBorderColor = Color(0xFFBCBFCD)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = state.phoneNumber,
            onValueChange = { newValue ->
                if (newValue.isDigitsOnly() && newValue.length <= 10) {
                    onEmailChange(newValue)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            isError = state.fieldErrors?.phoneNumberFieldError != null,
            supportingText = {
                state.fieldErrors?.phoneNumberFieldError?.let {
                    Text(text = it)
                }
            },
            prefix = { Text(stringResource(id = R.string.phone_number_prefix)) },
            label = { Text(stringResource(R.string.phone_number)) },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF5C49E0),
                unfocusedBorderColor = Color(0xFFBCBFCD)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = state.password,
            onValueChange = onPasswordChange,
            label = { Text(stringResource(R.string.password)) },
            shape = RoundedCornerShape(8.dp),
            isError = state.fieldErrors?.passwordFieldError != null,
            supportingText = {
                state.fieldErrors?.passwordFieldError?.let {
                    Text(text = it)
                }
            },
            visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF5C49E0),
                unfocusedBorderColor = Color(0xFFBCBFCD)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = state.repeatPassword,
            onValueChange = onRepeatPasswordChange,
            label = { Text(stringResource(R.string.repeat_password)) },
            shape = RoundedCornerShape(8.dp),
            isError = state.fieldErrors?.passwordConfirmationFieldError != null,
            supportingText = {
                state.fieldErrors?.passwordConfirmationFieldError?.let {
                    Text(text = it)
                }
            },
            visualTransformation = if (state.isRepeatPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = onChangeRepeatPasswordVisibility) {
                    Icon(
                        imageVector = if (state.isRepeatPasswordVisible)
                            ImageVector.vectorResource(id = R.drawable.visibility_on)
                        else
                            ImageVector.vectorResource(id = R.drawable.visibility_off),
                        contentDescription = "Toggle Password Visibility"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF5C49E0),
                unfocusedBorderColor = Color(0xFFBCBFCD)
            )
        )
    }
}
