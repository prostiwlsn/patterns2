package com.example.h_bankpro.presentation.user

import com.example.h_bankpro.data.Account
import com.example.h_bankpro.domain.model.Loan
import com.example.h_bankpro.domain.model.User
import java.time.LocalDateTime
import java.util.UUID

data class UserState(
    val user: User? = null,
    val accounts: List<Account> = emptyList(),
    val loans: List<Loan> = emptyList(),
    val isAccountsSheetVisible: Boolean = false,
    val isLoansSheetVisible: Boolean = false,
    val isLoading: Boolean = false
)