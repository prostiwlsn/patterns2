package com.example.h_bank.presentation.paymentHistory.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.h_bank.data.dto.payment.OperationTypeDto
import com.example.h_bank.presentation.paymentHistory.model.OperationShortModel
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun OperationItem(operation: OperationShortModel, onClick: () -> Unit) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale("ru"))
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale("ru"))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.run {
                size(40.dp)
                    .background(
                        color = Color(0xFF5C49E0),
                        shape = RoundedCornerShape(8.dp)
                    )
            },
            contentAlignment = Alignment.Center
        ) {
            val iconResId = when (operation.operationType) {
                OperationTypeDto.REPLENISHMENT -> R.drawable.add
                OperationTypeDto.WITHDRAWAL -> R.drawable.arrow_downward
                OperationTypeDto.TRANSFER -> if (operation.directionToMe) R.drawable.incoming_arrow
                else R.drawable.outgoing_arrow

                OperationTypeDto.LOAN_REPAYMENT -> R.drawable.dollar
            }
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                tint = Color.White
            )
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(
                text = "${operation.amount} ₽",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${operation.operationType.displayName} · ${
                    operation.transactionDateTime
                        .let { it.format(dateFormatter) + ", " + it.format(timeFormatter) }
                }",
                fontSize = 12.sp,
                color = Color(0xFF9B9CA1)
            )
        }
    }
}