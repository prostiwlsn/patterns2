package com.example.h_bankpro.di

import com.example.h_bankpro.presentation.login.LoginViewModel
import com.example.h_bankpro.presentation.registration.RegistrationViewModel
import com.example.h_bankpro.presentation.welcome.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { WelcomeViewModel() }
    viewModel { LoginViewModel() }
    viewModel { RegistrationViewModel() }
}
