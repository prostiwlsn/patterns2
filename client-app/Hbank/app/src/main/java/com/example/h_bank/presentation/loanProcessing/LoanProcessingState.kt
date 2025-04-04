package com.example.h_bank.presentation.loanProcessing

import androidx.paging.PagingData
import com.example.h_bank.data.Account
import com.example.h_bank.domain.entity.loan.TariffEntity
import com.example.h_bank.presentation.loanProcessing.model.LoanProcessingErrorModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

data class LoanProcessingState(
    val rateName: String = "",
    val rates: Flow<PagingData<TariffEntity>> = MutableStateFlow(PagingData.empty()),
    val selectedRate: TariffEntity? = null,
    val selectedAccount: Account? = null,
    val accounts: List<Account> = emptyList(),
    val amount: Int? = null,
    val term: Int? = null,
    val interestRate: Float? = null,
    val dailyPayment: Double? = null,
    val areFieldsValid: Boolean = false,
    val isRatesSheetVisible: Boolean = false,
    val isAccountsSheetVisible: Boolean = false,
    val fieldErrors: LoanProcessingErrorModel? = null,
    val errorMessage: String? = null,
)