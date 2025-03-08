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
    viewModel { WelcomeViewModel() }
    viewModel { MainViewModel(get(), get(), get(), get()) }
    viewModel { RateCreationViewModel(get()) }
    viewModel { SuccessfulRateCreationViewModel() }
    viewModel { SuccessfulRateEditingViewModel() }
    viewModel { SuccessfulRateDeletionViewModel() }
    viewModel { (savedStateHandle: SavedStateHandle) ->
        RateViewModel(savedStateHandle, get())
    }
    viewModel { (savedStateHandle: SavedStateHandle) ->
        RateEditingViewModel(savedStateHandle, get())
    }
    viewModel { (savedStateHandle: SavedStateHandle) ->
        LoanViewModel(savedStateHandle)
    }
    viewModel { UserCreationViewModel(get()) }
    viewModel { SuccessfulUserCreationViewModel() }
    viewModel { AccountViewModel(get(), get()) }
    viewModel { OperationInfoViewModel(get(), get()) }
    viewModel { (savedStateHandle: SavedStateHandle) ->
        UserViewModel(savedStateHandle, get(), get(), get(), get(), get())
    }
}
