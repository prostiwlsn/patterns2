package com.example.h_bank.di

import com.example.h_bank.data.network.LogoutApi
import com.example.h_bank.domain.useCase.authorization.LogoutUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val logoutModule = module {
    factory<LogoutApi> {
        val retrofit = get<Retrofit>(named("infoAuthApiWithAuth"))
        retrofit.create(LogoutApi::class.java)
    }
    factory<LogoutUseCase> {
        LogoutUseCase(
            authorizationRepository = get()
        )
    }
}