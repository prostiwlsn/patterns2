package com.example.h_bankpro.di

import com.example.h_bankpro.data.network.AuthorizationApi
import com.example.h_bankpro.data.repository.AuthorizationLocalStorage
import com.example.h_bankpro.data.repository.AuthorizationRemoteRepository
import com.example.h_bankpro.domain.repository.IAuthorizationLocalRepository
import com.example.h_bankpro.domain.repository.IAuthorizationRemoteRepository
import com.example.h_bankpro.domain.useCase.LoginUseCase
import com.example.h_bankpro.domain.useCase.storage.GetCredentialsFlowUseCase
import com.example.h_bankpro.domain.useCase.LoginValidationUseCase
import com.example.h_bankpro.domain.useCase.storage.UpdateCredentialsUseCase
import com.example.h_bankpro.presentation.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val authorizationModule = module {
    single<IAuthorizationLocalRepository> {
        AuthorizationLocalStorage()
    }

    factory<GetCredentialsFlowUseCase> {
        GetCredentialsFlowUseCase(
            storageRepository = get()
        )
    }

    factory<UpdateCredentialsUseCase> {
        UpdateCredentialsUseCase(
            storageRepository = get()
        )
    }

    factory<LoginValidationUseCase> {
        LoginValidationUseCase(
            storageRepository = get()
        )
    }

    factory<AuthorizationApi> {
        val retrofit = get<Retrofit>()
        retrofit.create(AuthorizationApi::class.java)
    }

    factory<IAuthorizationRemoteRepository> {
        AuthorizationRemoteRepository(
            storageRepository = get(),
            api = get(),
        )
    }

    factory<LoginUseCase> {
        LoginUseCase(
            authorizationRepository = get()
        )
    }

    viewModel {
        LoginViewModel(
            getCredentialsFlowUseCase = get(),
            updateCredentialsUseCase = get(),
            validationUseCase = get(),
            loginUseCase = get(),
        )
    }
}