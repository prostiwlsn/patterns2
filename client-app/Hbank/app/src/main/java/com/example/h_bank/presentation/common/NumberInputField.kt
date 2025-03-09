package com.example.h_bank.presentation.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.h_bank.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberInputField(
    @StringRes labelRes: Int,
    value: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    suffix: String = "",
    defaultValue: String = "",
    error: String? = null,
    onValueChange: (String) -> Unit,
    @StringRes errorMessageRes: Int? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            visualTransformation = visualTransformation,
            onValueChange = { text ->
                onValueChange(text.filter { it.isDigit() })
            },
            suffix = { Text(suffix) },
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color(0xFF282A31),
                fontWeight = FontWeight.Medium
            ),
            isError = error != null,
            supportingText = { Text(error.orEmpty()) },
            placeholder = { Text(defaultValue) },
            label = { Text(stringResource(labelRes)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF5C49E0),
                unfocusedBorderColor = Color(0xFFBCBFCD)
            )
        )
        if (errorMessageRes != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(errorMessageRes),
                color = Color.Red,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}