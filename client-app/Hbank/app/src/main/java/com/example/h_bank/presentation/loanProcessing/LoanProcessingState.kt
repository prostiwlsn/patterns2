package com.example.h_bank.presentation.loanProcessing

import com.example.h_bank.data.Rate
import com.example.h_bank.presentation.loanProcessing.model.LoanProcessingErrorModel

data class LoanProcessingState(
    val rateName: String = "",
    val rates: List<Rate> = listOf(
        Rate("1", "Тариф  1",10f),
        Rate("2", "Тариф  2",20f),
        Rate("3", "Тариф  3",30f),
    ),
    val selectedRate: Rate = Rate("1", "Тариф  1",10f),
    val amount: Int? = null,
    val term: Int? = null,
    val interestRate: Float? = null,
    val dailyPayment: Int? = null,
    val areFieldsValid: Boolean = false,
    val isRatesSheetVisible: Boolean = false,
    val fieldErrors: LoanProcessingErrorModel? = null,
)