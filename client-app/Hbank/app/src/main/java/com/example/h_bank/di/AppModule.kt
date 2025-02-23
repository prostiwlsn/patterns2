package com.example.h_bank.di

import com.example.h_bank.presentation.login.LoginViewModel
import com.example.h_bank.presentation.registration.RegistrationViewModel
import com.example.h_bank.presentation.welcome.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { WelcomeViewModel() }
    viewModel { LoginViewModel() }
    viewModel { RegistrationViewModel() }
}
