package com.example.h_bankpro.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.h_bankpro.R

@Composable
fun SuccessIcon() {
    Box(
        modifier = Modifier
            .size(78.dp)
            .background(Color(0xFF5C49E0), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.check),
            contentDescription = "Success",
            tint = Color.White,
            modifier = Modifier.size(46.dp)
        )
    }
}
