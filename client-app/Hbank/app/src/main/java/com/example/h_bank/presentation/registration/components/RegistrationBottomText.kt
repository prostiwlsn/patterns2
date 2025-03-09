package com.example.h_bank.presentation.registration.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.h_bank.R

@Composable
fun RegistrationBottomText(
    onLoginClick: () -> Unit
) {
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
            modifier = Modifier.clickable { onLoginClick() }
        )
    }
}
