package com.example.h_bankpro.di

import androidx.lifecycle.SavedStateHandle
import com.example.h_bankpro.presentation.account.AccountViewModel
import com.example.h_bankpro.presentation.loan.LoanViewModel
import com.example.h_bankpro.presentation.main.MainViewModel
import com.example.h_bankpro.presentation.rate.RateViewModel
import com.example.h_bankpro.presentation.rateCreation.RateCreationViewModel
import com.example.h_bankpro.presentation.rateEditing.RateEditingViewModel
import com.example.h_bankpro.presentation.successfulRateCreation.SuccessfulRateCreationViewModel
import com.example.h_bankpro.presentation.successfulRateEditing.SuccessfulRateEditingViewModel
import com.example.h_bankpro.presentation.successfulUserCreation.SuccessfulUserCreationViewModel
import com.example.h_bankpro.presentation.transactionInfo.TransactionInfoViewModel
import com.example.h_bankpro.presentation.user.UserViewModel
import com.example.h_bankpro.presentation.userCreation.UserCreationViewModel
import com.example.h_bankpro.presentation.welcome.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { WelcomeViewModel() }
    viewModel { MainViewModel(get(), get()) }
    viewModel { RateCreationViewModel() }
    viewModel { SuccessfulRateCreationViewModel() }
    viewModel { SuccessfulRateEditingViewModel() }
    viewModel { RateViewModel() }
    viewModel { RateEditingViewModel() }
    viewModel { LoanViewModel() }
    viewModel { UserCreationViewModel(get()) }
    viewModel { SuccessfulUserCreationViewModel() }
    viewModel { AccountViewModel(get(), get()) }
    viewModel { TransactionInfoViewModel(get(), get()) }
    viewModel { (savedStateHandle: SavedStateHandle) ->
        UserViewModel(
            savedStateHandle,
            get(),
            get(),
            get(),
            get()
        )
    }
}
