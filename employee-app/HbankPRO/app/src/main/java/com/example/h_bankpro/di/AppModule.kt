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
import com.example.h_bankpro.presentation.operationInfo.OperationInfoViewModel
import com.example.h_bankpro.presentation.successfulRateDeletion.SuccessfulRateDeletionViewModel
import com.example.h_bankpro.presentation.user.UserViewModel
import com.example.h_bankpro.presentation.userCreation.UserCreationViewModel
import com.example.h_bankpro.presentation.welcome.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { WelcomeViewModel(get()) }
    viewModel { MainViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { SuccessfulRateCreationViewModel(get()) }
    viewModel { SuccessfulRateEditingViewModel(get()) }
    viewModel { SuccessfulRateDeletionViewModel(get()) }
    viewModel { (savedStateHandle: SavedStateHandle) ->
        RateViewModel(get(), savedStateHandle, get())
    }
    viewModel { (savedStateHandle: SavedStateHandle) ->
        RateEditingViewModel(get(), savedStateHandle, get())
    }
    viewModel { (savedStateHandle: SavedStateHandle) ->
        LoanViewModel(get(), savedStateHandle)
    }
    viewModel { UserCreationViewModel(get(), get()) }
    viewModel { SuccessfulUserCreationViewModel(get()) }
    viewModel { AccountViewModel(get(), get(), get()) }
    viewModel { OperationInfoViewModel(get(), get(), get()) }
    viewModel { (savedStateHandle: SavedStateHandle) ->
        UserViewModel(get(), savedStateHandle, get(), get(), get(), get(), get())
    }
}
