package com.example.h_bank.presentation.paymentHistory.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FilterButton(
    text: String,
    isCloseable: Boolean,
    onClick: () -> Unit,
    onClose: () -> Unit,
    isActive: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                color = if (isActive) Color(0xFF5C49E0) else Color(0xFFD9D9D9),
            )
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = if (isActive) Color.White else Color.Black
        )
        if (isActive && isCloseable){
            Icon(
                modifier = Modifier
                    .clickable {
                        onClose()
                    },
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Color.White
            )
        } else {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = if (isActive) Color.White else Color(0xFF585858)
            )
        }
    }
}