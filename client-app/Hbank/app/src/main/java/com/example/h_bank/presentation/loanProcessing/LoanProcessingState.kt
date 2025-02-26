package com.example.h_bank.presentation.loanProcessing

import com.example.h_bank.data.Rate

data class LoanProcessingState(
    val rateName: String = "",
    val rates: List<Rate> = listOf(
        Rate("1", "Тариф  1, ",10f),
        Rate("2", "Тариф  2, ",20f),
        Rate("3", "Тариф  3, ",30f),
    ),
    val selectedRate: Rate = Rate("1", "Тариф  1, ",10f),
    val amount: Int = 0,
    val term: Int = 0,
    val interestRate: Float = 0f,
    val dailyPayment: Int = 1,
    val areFieldsValid: Boolean = false,
    val isRatesSheetVisible: Boolean = false
)