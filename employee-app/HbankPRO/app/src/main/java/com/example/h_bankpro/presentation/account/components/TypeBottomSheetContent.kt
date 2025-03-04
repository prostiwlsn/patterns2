package com.example.h_bankpro.presentation.account.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.h_bankpro.data.PaymentType
import com.example.h_bankpro.data.PaymentTypeFilter

@Composable
fun TypeBottomSheetContent(
    onItemClick: (PaymentTypeFilter) -> Unit
) {
    Column(Modifier.padding(16.dp)) {
        Text(
            text = PaymentTypeFilter.All.displayName,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onItemClick(PaymentTypeFilter.All)
                }
                .padding(16.dp)
        )
        PaymentType.entries.forEach { type ->
            Text(
                text = type.displayName,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(PaymentTypeFilter.Specific(type))
                    }
                    .padding(16.dp)
            )
        }
    }
}