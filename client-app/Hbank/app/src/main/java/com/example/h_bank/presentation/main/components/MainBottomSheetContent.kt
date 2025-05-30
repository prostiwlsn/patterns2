package com.example.h_bank.presentation.main.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.h_bank.data.Account
import com.example.h_bank.data.Loan
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.h_bank.R
import com.example.h_bank.data.dto.CurrencyDto
import com.example.h_bank.presentation.common.AccountItem
import com.example.h_bank.presentation.common.CurrencyItem
import com.example.h_bank.presentation.common.CustomButton

@Composable
fun AccountsBottomSheetContent(
    accounts: List<Account>,
    hiddenAccounts: Set<String>,
    onToggleVisibility: (String) -> Unit,
    onCloseAccountClick: (Account) -> Unit,
) {
    var showHidden by remember { mutableStateOf(false) }
    val filteredAccounts =
        if (showHidden) accounts.filter { hiddenAccounts.contains(it.id) } else accounts.filter {
            !hiddenAccounts.contains(it.id)
        }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(if (showHidden) R.string.hidden_accounts else R.string.all_accounts),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Switch(
                    checked = showHidden,
                    onCheckedChange = { showHidden = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        uncheckedThumbColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f),
                        checkedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f),
                        uncheckedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f),

                        )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(filteredAccounts) { account ->
            AccountItem(
                account = account,
                isHidden = hiddenAccounts.contains(account.id),
                onToggleVisibility = onToggleVisibility,
                onCloseAccountClick = onCloseAccountClick
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )
        }
    }
}

@Composable
fun LoansBottomSheetContent(
    lazyPagingItems: LazyPagingItems<Loan>,
    onItemClick: (Loan) -> Unit,
    onProcessLoanClick: () -> Unit,
    creditRating: Int?
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(R.string.all_loans),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (creditRating != null) {
                Text(
                    text = "Social Credit: $creditRating",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(lazyPagingItems.itemCount) { index ->
            val loan = lazyPagingItems[index]
            if (loan != null) {
                LoanItem(
                    loan = loan,
                    onClick = { onItemClick(loan) }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )
            }
        }
        when (lazyPagingItems.loadState.append) {
            is LoadState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            is LoadState.Error -> {
                item {
                    Text(
                        text = "Error loading more loans",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> {}
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

@Composable
fun CurrenciesBottomSheetContent(
    onCurrencyClick: (CurrencyDto) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            Text(
                text = stringResource(R.string.choose_currency),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(CurrencyDto.entries.toTypedArray()) { currency ->
            CurrencyItem(
                currency = currency,
                onCurrencyClick = { onCurrencyClick(currency) }
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )
        }
    }
}