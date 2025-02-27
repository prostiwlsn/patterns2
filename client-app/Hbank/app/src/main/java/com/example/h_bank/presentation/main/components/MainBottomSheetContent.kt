package com.example.h_bank.presentation.main.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.h_bank.data.Account
import com.example.h_bank.data.Loan
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.h_bank.R
import com.example.h_bank.presentation.common.AccountItem
import com.example.h_bank.presentation.common.CustomButton

@Composable
fun AccountsBottomSheetContent(
    accounts: List<Account>,
    onItemClick: (Account) -> Unit,
    onOpenAccountClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.all_accounts),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(accounts) { account ->
            AccountItem(account = account, onClick = { onItemClick(account) })
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = Color(0xFFD9D9D9)
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            CustomButton(
                onClick = onOpenAccountClick,
                textRes = R.string.main_screen_bottom_sheet_account_button
            )
        }
    }
}

@Composable
fun LoansBottomSheetContent(
    loans: List<Loan>,
    onItemClick: (Loan) -> Unit,
    onProcessLoanClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.all_loans),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(loans) { loan ->
            LoanItem(loan = loan, onClick = { onItemClick(loan) })
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = Color(0xFFD9D9D9)
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            CustomButton(
                onClick = onProcessLoanClick,
                textRes = R.string.main_screen_bottom_sheet_credit_button
            )
        }
    }
}