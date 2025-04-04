package com.example.h_bank.presentation.loan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.h_bank.R
import com.example.h_bank.presentation.paymentHistory.model.OperationShortModel
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ExpiredLoanItem(operation: OperationShortModel) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale("ru"))
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale("ru"))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.run {
                size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    )
            },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.dollar),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(
                text = "${operation.amount} ₽",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "${operation.operationType.displayName} · ${
                    operation.transactionDateTime
                        .let { it.format(dateFormatter) + ", " + it.format(timeFormatter) }
                }",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}