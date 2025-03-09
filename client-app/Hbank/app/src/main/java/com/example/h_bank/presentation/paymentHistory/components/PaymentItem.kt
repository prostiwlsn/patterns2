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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.h_bank.R
import com.example.h_bank.domain.entity.payment.PaymentItemEntity
import com.example.h_bank.domain.entity.filter.OperationType
import com.example.h_bank.domain.entity.filter.OperationType.Companion.getText
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PaymentItem(payment: PaymentItemEntity, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color(0xFF5C49E0),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_downward),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.rotate(if (payment.type == OperationType.REPLENISHMENT) 0f else 180f)
            )
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(
                text = "${payment.amount} ₽",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${payment.type.getText()} · ${
                    payment.date.format(
                        DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("ru"))
                    )
                }",
                fontSize = 12.sp,
                color = Color(0xFF9B9CA1)
            )
        }
    }
}