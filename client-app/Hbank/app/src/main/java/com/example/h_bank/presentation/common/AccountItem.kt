package com.example.h_bank.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.h_bank.R
import com.example.h_bank.data.Account

@Composable
fun AccountItem(
    account: Account,
    onCloseAccountClick: (Account) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = account.accountNumber, fontSize = 14.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = account.balance.toBigDecimal().toPlainString() + " ₽", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        IconButton(onClick = { onCloseAccountClick(account) }) {
            Icon(
                painter = painterResource(id = R.drawable.delete),
                contentDescription = "Back",
                modifier = Modifier.size(24.dp),
                tint = Color.Red
            )
        }
    }
}