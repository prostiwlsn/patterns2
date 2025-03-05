package com.example.h_bankpro.di

import com.example.h_bankpro.data.network.AuthorizationApi
import com.example.h_bankpro.data.repository.AuthorizationLocalStorage
import com.example.h_bankpro.data.repository.AuthorizationRemoteRepository
import com.example.h_bankpro.domain.repository.IAuthorizationLocalRepository
import com.example.h_bankpro.domain.repository.IAuthorizationRemoteRepository
import com.example.h_bankpro.domain.repository.IUserRepository
import com.example.h_bankpro.domain.useCase.LoginUseCase
import com.example.h_bankpro.domain.useCase.storage.GetCredentialsFlowUseCase
import com.example.h_bankpro.domain.useCase.LoginValidationUseCase
import com.example.h_bankpro.domain.useCase.RefreshTokenUseCase
import com.example.h_bankpro.domain.useCase.RegisterUseCase
import com.example.h_bankpro.domain.useCase.RegistrationValidationUseCase
import com.example.h_bankpro.domain.useCase.SaveTokenUseCase
import com.example.h_bankpro.domain.useCase.storage.ResetCredentialsUseCase
import com.example.h_bankpro.domain.useCase.storage.UpdateCredentialsUseCase
import com.example.h_bankpro.presentation.login.LoginViewModel
import com.example.h_bankpro.presentation.registration.RegistrationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val authorizationModule = module {
    factory<RefreshTokenUseCase> {
        RefreshTokenUseCase(
            remoteRepository = get(),
            tokenStorage = get()
        )
    }

    factory<SaveTokenUseCase> {
        SaveTokenUseCase(
            tokenStorage = get()
        )
    }

    factory<IAuthorizationRemoteRepository> {
        AuthorizationRemoteRepository(
            storageRepository = get(),
            tokenStorage = get(),
            api = get()
        )
    }

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
        val retrofit = get<Retrofit>(named("firstApi"))
        retrofit.create(AuthorizationApi::class.java)
    }

    factory<IAuthorizationRemoteRepository> {
        AuthorizationRemoteRepository(
            storageRepository = get(),
            api = get(),
            tokenStorage = get(),
        )
    }

    factory<LoginUseCase> {
        LoginUseCase(
            authorizationRepository = get()
        )
    }

    factory<ResetCredentialsUseCase> {
        ResetCredentialsUseCase(
            storageRepository = get(),
        )
    }

    factory<RegistrationValidationUseCase> {
        RegistrationValidationUseCase(
            storageRepository = get(),
        )
    }

    factory<RegisterUseCase> {
        RegisterUseCase(
            authorizationRepository = get(),
            storageRepository = get(),
        )
    }

    viewModel {
        LoginViewModel(
            getCredentialsFlowUseCase = get(),
            updateCredentialsUseCase = get(),
            validationUseCase = get(),
            loginUseCase = get(),
            resetCredentialsUseCase = get(),
        )
    }

    viewModel {
        RegistrationViewModel(
            updateCredentialsUseCase = get(),
            getCredentialsFlowUseCase = get(),
            resetCredentialsUseCase = get(),
            validationUseCase = get(),
            registerUseCase = get(),
        )
    }
}