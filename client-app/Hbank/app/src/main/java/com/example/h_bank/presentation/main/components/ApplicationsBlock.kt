package com.example.h_bank.presentation.main.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.h_bank.R

@Composable
fun ApplicationsBlock(
    onProcessLoanClick: () -> Unit,
    onOpenAccountClick: () -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.applications),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Color(0xFFD9D9D9))
        ) {
            Column {
                ApplicationItem(
                    iconRes = R.drawable.add,
                    text = stringResource(R.string.take_out_loan),
                    onClick = onProcessLoanClick
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    thickness = 1.dp,
                    color = Color(0xFFD9D9D9)
                )
                ApplicationItem(
                    iconRes = R.drawable.add,
                    text = stringResource(R.string.open_account),
                    onClick = onOpenAccountClick
                )
            }
        }
    }
}