package com.example.h_bankpro.presentation.user

import androidx.paging.PagingData
import com.example.h_bankpro.data.Account
import com.example.h_bankpro.domain.model.Loan
import com.example.h_bankpro.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

data class UserState(
    val initialLoans: List<Loan> = emptyList(),
    val loansFlow: Flow<PagingData<Loan>> = MutableStateFlow(PagingData.empty()),
    val user: User? = null,
    val accounts: List<Account> = emptyList(),
    val isAccountsSheetVisible: Boolean = false,
    val isLoansSheetVisible: Boolean = false,
    val isLoading: Boolean = false
)