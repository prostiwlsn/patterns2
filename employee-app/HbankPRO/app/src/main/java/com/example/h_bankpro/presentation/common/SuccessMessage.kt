package com.example.h_bankpro.presentation.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun SuccessMessage(
    @StringRes textRes: Int
) {
    Text(
        text = stringResource(textRes),
        fontSize = 30.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF282A31),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}