package com.example.h_bank.di

import com.example.h_bank.data.network.AuthorizationApi
import com.example.h_bank.data.repository.authorization.AuthorizationRemoteRepository
import com.example.h_bank.domain.repository.authorization.IAuthorizationLocalRepository
import com.example.h_bank.domain.repository.authorization.IAuthorizationRemoteRepository
import com.example.h_bank.domain.repository.authorization.ITokenStorage
import com.example.h_bank.domain.repository.authorization.TokenLocalStorage
import com.example.h_bank.domain.useCase.authorization.LoginUseCase
import com.example.h_bank.domain.useCase.storage.GetCredentialsFlowUseCase
import com.example.h_bank.domain.useCase.authorization.RefreshTokenUseCase
import com.example.h_bank.domain.useCase.authorization.RegisterUseCase
import com.example.h_bank.domain.useCase.SaveTokenUseCase
import com.example.h_bank.domain.useCase.storage.ResetCredentialsUseCase
import com.example.h_bank.domain.useCase.storage.UpdateCredentialsUseCase
import com.example.h_bank.data.repository.authorization.AuthorizationLocalStorage
import com.example.h_bank.domain.useCase.GetUserIdUseCase
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
            tokenRepository = get()
        )
    }

    factory<IAuthorizationRemoteRepository> {
        AuthorizationRemoteRepository(
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
}