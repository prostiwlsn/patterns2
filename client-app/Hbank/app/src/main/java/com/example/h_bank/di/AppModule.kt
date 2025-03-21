package com.example.h_bank.di

import androidx.lifecycle.SavedStateHandle
import com.example.h_bank.presentation.connectionErrorScreen.ConnectionErrorViewModel
import com.example.h_bank.presentation.launch.LaunchViewModel
import com.example.h_bank.presentation.loan.LoanViewModel
import com.example.h_bank.presentation.loanPayment.LoanPaymentViewModel
import com.example.h_bank.presentation.main.MainViewModel
import com.example.h_bank.presentation.navigation.NavigationViewModel
import com.example.h_bank.presentation.paymentHistory.PaymentHistoryViewModel
import com.example.h_bank.presentation.replenishment.ReplenishmentViewModel
import com.example.h_bank.presentation.successfulAccountClosure.SuccessfulAccountClosureViewModel
import com.example.h_bank.presentation.successfulAccountOpening.SuccessfulAccountOpeningViewModel
import com.example.h_bank.presentation.successfulLoanPayment.SuccessfulLoanPaymentViewModel
import com.example.h_bank.presentation.successfulLoanProcessing.SuccessfulLoanProcessingViewModel
import com.example.h_bank.presentation.successfulReplenishment.SuccessfulReplenishmentViewModel
import com.example.h_bank.presentation.successfulTransfer.SuccessfulTransferViewModel
import com.example.h_bank.presentation.successfulWithdrawal.SuccessfulWithdrawalViewModel
import com.example.h_bank.presentation.transactionInfo.TransactionInfoViewModel
import com.example.h_bank.presentation.transfer.TransferViewModel
import com.example.h_bank.presentation.welcome.WelcomeViewModel
import com.example.h_bank.presentation.withdrawal.WithdrawalViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { WelcomeViewModel(get()) }
    viewModel { LaunchViewModel(get(), get()) }
    viewModel { MainViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { (savedStateHandle: SavedStateHandle) ->
        LoanPaymentViewModel(
            get(),
            savedStateHandle,
            get(),
            get(),
            get()
        )
    }
    viewModel { SuccessfulAccountClosureViewModel(get()) }
    viewModel { NavigationViewModel(getAuthorizationCommandsUseCase = get(), pushCommandUseCase = get()) }
    viewModel { ConnectionErrorViewModel() }
    viewModel { SuccessfulLoanProcessingViewModel(get()) }
    viewModel { SuccessfulLoanPaymentViewModel(get(), get()) }
    viewModel { SuccessfulAccountOpeningViewModel(get()) }
    viewModel { TransferViewModel(get(), get(), get(), get(), get()) }
    viewModel { ReplenishmentViewModel(get(), get(), get(), get()) }
    viewModel { (savedStateHandle: SavedStateHandle) ->
        WithdrawalViewModel(
            get(),
            savedStateHandle,
            get(),
            get()
        )
    }
    viewModel { (savedStateHandle: SavedStateHandle) ->
        TransactionInfoViewModel(
            pushCommandUseCase = get(),
            savedStateHandle = savedStateHandle,
            getOperationDetailsUseCase = get(),
        )
    }
    viewModel { (savedStateHandle: SavedStateHandle) -> LoanViewModel(get(), savedStateHandle) }
    viewModel { (savedStateHandle: SavedStateHandle) ->
        SuccessfulTransferViewModel(savedStateHandle)
    }
    viewModel { (savedStateHandle: SavedStateHandle) ->
        SuccessfulWithdrawalViewModel(
            get(),
            savedStateHandle
        )
    }
    viewModel { (savedStateHandle: SavedStateHandle) ->
        SuccessfulReplenishmentViewModel(
            get(),
            savedStateHandle
        )
    }
}
