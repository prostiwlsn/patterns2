package com.example.h_bankpro.presentation.loan

data class LoanState(
    val documentNumber: String = "",
    val amount: String = "",
    val endDate: String  = "",
    val ratePercent: String  = "",
    val debt: String  = ""
)