package com.example.h_bankpro.presentation.user.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.h_bankpro.R
import com.example.h_bankpro.data.RoleType

@Composable
fun UserHeader(
    name: String,
    roles: List<RoleType>,
    isBlocked: Boolean,
    onBackClick: () -> Unit,
    onBlockClick: () -> Unit,
    onUnblockClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
        Spacer(modifier = Modifier.weight(0.7f))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (roles.isEmpty()) stringResource(R.string.client) else roles.joinToString(
                    ", "
                ) { it.displayName },
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF9B9CA1)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = if (isBlocked) onUnblockClick else onBlockClick) {
            Icon(
                painter = if (isBlocked) painterResource(id = R.drawable.check) else
                    painterResource(id = R.drawable.block),
                tint = if (isBlocked) Color.Green else Color.Red,
                contentDescription = ""
            )
        }
    }
}
