package com.example.h_bank.presentation.paymentHistory.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.h_bank.domain.entity.filter.OperationType
import com.example.h_bank.domain.entity.filter.OperationType.Companion.getText

@Composable
fun TypeBottomSheetContent(
    onItemClick: (OperationType) -> Unit
) {
    Column(Modifier.padding(16.dp)) {
        OperationType.entries.forEach { type ->
            Text(
                text = type.getText(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(type)
                    }
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}