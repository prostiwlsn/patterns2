package com.example.h_bankpro.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.h_bankpro.R
import com.example.h_bankpro.data.Account

@Composable
fun AccountItem(
    account: Account,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = account.id, fontSize = 14.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = account.balance.toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Icon(
            painter = painterResource(id = R.drawable.credit_card),
            contentDescription = "Credit Card Icon",
            modifier = Modifier.size(24.dp),
            tint = Color.Black
        )
    }
}