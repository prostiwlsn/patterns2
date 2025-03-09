package com.example.h_bankpro.di

import com.example.h_bankpro.data.network.AuthorizationApi
import com.example.h_bankpro.data.repository.AuthorizationLocalStorage
import com.example.h_bankpro.data.repository.AuthorizationRemoteRepository
import com.example.h_bankpro.data.repository.CommandStorage
import com.example.h_bankpro.domain.repository.IAuthorizationLocalRepository
import com.example.h_bankpro.domain.repository.IAuthorizationRemoteRepository
import com.example.h_bankpro.domain.repository.ICommandStorage
import com.example.h_bankpro.domain.useCase.GetCommandUseCase
import com.example.h_bankpro.domain.useCase.LoginUseCase
import com.example.h_bankpro.domain.useCase.storage.GetCredentialsFlowUseCase
import com.example.h_bankpro.domain.useCase.LoginValidationUseCase
import com.example.h_bankpro.domain.useCase.PushCommandUseCase
import com.example.h_bankpro.domain.useCase.RegisterUseCase
import com.example.h_bankpro.domain.useCase.RegistrationValidationUseCase
import com.example.h_bankpro.domain.useCase.storage.ResetCredentialsUseCase
import com.example.h_bankpro.domain.useCase.storage.UpdateCredentialsUseCase
import com.example.h_bankpro.presentation.common.viewModel.BaseViewModel
import com.example.h_bankpro.presentation.connectionError.ConnectionErrorScreen
import com.example.h_bankpro.presentation.connectionError.ConnectionErrorViewModel
import com.example.h_bankpro.presentation.launch.LaunchViewModel
import com.example.h_bankpro.presentation.login.LoginViewModel
import com.example.h_bankpro.presentation.navigation.NavigationViewModel
import com.example.h_bankpro.presentation.registration.RegistrationViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val authorizationModule = module {

    single<IAuthorizationLocalRepository> {
        AuthorizationLocalStorage(androidContext())
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

    single<IAuthorizationRemoteRepository> {
        AuthorizationRemoteRepository(
            storageRepository = get(),
            authApi = get(),
            tokenRepository = get(),
            logoutApi = get(),
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

    single<ICommandStorage> {
        CommandStorage()
    }

    factory<PushCommandUseCase> {
        PushCommandUseCase(
            storage = get(),
        )
    }

    factory<GetCommandUseCase> {
        GetCommandUseCase(
            storage = get(),
        )
    }

    viewModel { LaunchViewModel(get(), get()) }
    viewModel { NavigationViewModel(get(), get()) }
    viewModel { ConnectionErrorViewModel(get()) }

    viewModel {
        LoginViewModel(
            getCredentialsFlowUseCase = get(),
            updateCredentialsUseCase = get(),
            validationUseCase = get(),
            loginUseCase = get(),
            resetCredentialsUseCase = get(),
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
            pushCommandUseCase = get(),
        )
    }
}