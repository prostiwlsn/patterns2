package com.example.h_bank.di

import com.example.h_bank.data.network.AuthorizationApi
import com.example.h_bank.data.repository.authorization.AuthorizationRemoteRepository
import com.example.h_bank.domain.repository.authorization.IAuthorizationLocalRepository
import com.example.h_bank.domain.repository.authorization.IAuthorizationRemoteRepository
import com.example.h_bank.domain.repository.authorization.ITokenStorage
import com.example.h_bank.domain.repository.authorization.TokenLocalStorage
import com.example.h_bank.domain.useCase.authorization.LoginUseCase
import com.example.h_bank.domain.useCase.storage.GetCredentialsFlowUseCase
import com.example.h_bank.domain.useCase.authorization.LoginValidationUseCase
import com.example.h_bank.domain.useCase.authorization.RefreshTokenUseCase
import com.example.h_bank.domain.useCase.authorization.RegisterUseCase
import com.example.h_bank.domain.useCase.authorization.RegistrationValidationUseCase
import com.example.h_bank.domain.useCase.SaveTokenUseCase
import com.example.h_bank.domain.useCase.storage.ResetCredentialsUseCase
import com.example.h_bank.domain.useCase.storage.UpdateCredentialsUseCase
import com.example.h_bank.presentation.login.LoginViewModel
import com.example.h_bank.presentation.registration.RegistrationViewModel
import com.example.h_bank.data.repository.authorization.AuthorizationLocalStorage
import com.example.h_bank.domain.useCase.GetUserIdUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val authorizationModule = module {
    factory<RefreshTokenUseCase> {
        RefreshTokenUseCase(
            localRepository = get(),
            tokenRepository = get(),
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
            authApi = get(),
            logoutApi = get(),
            tokenRepository = get(),
        )
    }

    single<IAuthorizationLocalRepository> {
        AuthorizationLocalStorage(
            context = get()
        )
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
        val retrofit = get<Retrofit>(named("infoAuthApi"))
        retrofit.create(AuthorizationApi::class.java)
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

    single<ITokenStorage> {
        TokenLocalStorage(
            context = get()
        )
    }

    factory<GetUserIdUseCase> {
        GetUserIdUseCase(
            storageRepository = get()
        )
    }

    viewModel {
        LoginViewModel(
            getCredentialsFlowUseCase = get(),
            updateCredentialsUseCase = get(),
            validationUseCase = get(),
            loginUseCase = get(),
            resetCredentialsUseCase = get(),
            pushAuthorizationCommandUseCase = get(),
            pushCommandUseCase = get(),
        )
    }

    viewModel {
        RegistrationViewModel(
            updateCredentialsUseCase = get(),
            getCredentialsFlowUseCase = get(),
            resetCredentialsUseCase = get(),
            validationUseCase = get(),
            registerUseCase = get(),
            pushAuthorizationCommandUseCase = get(),
            pushCommandUseCase = get(),
        )
    }
}