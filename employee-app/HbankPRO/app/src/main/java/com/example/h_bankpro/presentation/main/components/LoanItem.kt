package com.example.h_bankpro.presentation.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.h_bankpro.data.Loan
import java.time.format.DateTimeFormatter

@Composable
fun LoanItem(
    loan: Loan,
    onClick: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yy")
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Column {
            Text(
                text = "Договор № " + loan.documentNumber.toString(),
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = loan.amount.toString() + " ₽", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Text(
            text = "до " + loan.endDate.format(formatter),
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 4.dp)
        )
    }
}