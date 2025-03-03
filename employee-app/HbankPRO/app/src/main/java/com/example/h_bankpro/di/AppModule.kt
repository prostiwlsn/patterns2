package com.example.h_bankpro.di

import androidx.lifecycle.SavedStateHandle
import com.example.h_bankpro.presentation.login.LoginViewModel
import com.example.h_bankpro.presentation.main.MainViewModel
import com.example.h_bankpro.presentation.rateCreation.RateCreationViewModel
import com.example.h_bankpro.presentation.rateEditing.RateEditingViewModel
import com.example.h_bankpro.presentation.registration.RegistrationViewModel
import com.example.h_bankpro.presentation.successfulRateCreation.SuccessfulRateCreationViewModel
import com.example.h_bankpro.presentation.successfulRateEditing.SuccessfulRateEditingViewModel
import com.example.h_bankpro.presentation.welcome.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { WelcomeViewModel() }
    viewModel { LoginViewModel() }
    viewModel { RegistrationViewModel() }
    viewModel { MainViewModel() }
    viewModel { RateCreationViewModel() }
    viewModel { SuccessfulRateCreationViewModel() }
    viewModel { SuccessfulRateEditingViewModel() }
    viewModel { (savedStateHandle: SavedStateHandle) -> RateEditingViewModel(savedStateHandle) }
}
