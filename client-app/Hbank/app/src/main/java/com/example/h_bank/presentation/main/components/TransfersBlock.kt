package com.example.h_bank.presentation.main.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.h_bank.R

@Composable
fun TransfersBlock(
    onTransferClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onReplenishmentClick: () -> Unit,
    onWithdrawalClick: () -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.transfers),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
        ) {
            Column {
                ApplicationItem(
                    iconRes = R.drawable.transfer,
                    text = stringResource(R.string.transfer),
                    onClick = onTransferClick
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )
                ApplicationItem(
                    iconRes = R.drawable.history,
                    text = stringResource(R.string.operation_history),
                    onClick = onHistoryClick
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )
                ApplicationItem(
                    iconRes = R.drawable.dollar,
                    text = stringResource(R.string.replenish),
                    onClick = onReplenishmentClick
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )
                ApplicationItem(
                    iconRes = R.drawable.dollar,
                    text = stringResource(R.string.withdraw),
                    onClick = onWithdrawalClick
                )
            }
        }
    }
}