package com.example.h_bankpro.di

import com.example.h_bankpro.data.dataSource.local.AuthorizationLocalDataSource
import com.example.h_bankpro.data.dataSource.remote.AuthorizationRemoteDataSource
import com.example.h_bankpro.data.network.AuthorizationApi
import com.example.h_bankpro.data.repository.AuthorizationLocalRepository
import com.example.h_bankpro.data.repository.AuthorizationRemoteRepository
import com.example.h_bankpro.data.repository.CommandStorage
import com.example.h_bankpro.domain.repository.IAuthorizationLocalRepository
import com.example.h_bankpro.domain.repository.IAuthorizationRemoteRepository
import com.example.h_bankpro.domain.repository.ICommandStorage
import com.example.h_bankpro.domain.useCase.GetCommandUseCase
import com.example.h_bankpro.domain.useCase.LoginUseCase
import com.example.h_bankpro.domain.useCase.storage.GetCredentialsFlowUseCase
import com.example.h_bankpro.domain.useCase.PushCommandUseCase
import com.example.h_bankpro.domain.useCase.RegisterUseCase
import com.example.h_bankpro.domain.useCase.storage.ResetCredentialsUseCase
import com.example.h_bankpro.domain.useCase.storage.UpdateCredentialsUseCase
import com.example.h_bankpro.presentation.connectionError.ConnectionErrorViewModel
import com.example.h_bankpro.presentation.launch.LaunchViewModel
import com.example.h_bankpro.presentation.navigation.NavigationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val authorizationModule = module {

    single { AuthorizationLocalDataSource(context = get()) }

    single<IAuthorizationLocalRepository> {
        AuthorizationLocalRepository(
            localDataSource = get()
        )
    }

    single { AuthorizationRemoteDataSource(logoutApi = get()) }

    single<IAuthorizationRemoteRepository> {
        AuthorizationRemoteRepository(
            remoteDataSource = get(),
            tokenRepository = get()
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

    viewModel { LaunchViewModel(get(), get(), get()) }
    viewModel { NavigationViewModel(get(), get()) }
    viewModel { ConnectionErrorViewModel(get()) }
}