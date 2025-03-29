package com.example.h_bank.presentation.main

import androidx.paging.PagingData
import com.example.h_bank.data.Account
import com.example.h_bank.data.Loan
import com.example.h_bank.data.ThemeMode
import com.example.h_bank.data.dto.CurrencyDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

data class MainState(
    val currentUserId: String = "",
    val accounts: List<Account> = emptyList(),
    val initialLoans: List<Loan> = emptyList(),
    val loansFlow: Flow<PagingData<Loan>> = MutableStateFlow(PagingData.empty()),
    val isAccountsSheetVisible: Boolean = false,
    val isLoansSheetVisible: Boolean = false,
    val isLoading: Boolean = false,
    val selectedCurrency: CurrencyDto = CurrencyDto.RUB,
    val hiddenAccounts: Set<String> = emptySet(),
    val themeMode: ThemeMode = ThemeMode.LIGHT
)