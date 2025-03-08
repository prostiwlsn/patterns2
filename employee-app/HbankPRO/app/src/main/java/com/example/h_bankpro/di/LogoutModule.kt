package com.example.h_bankpro.di

import com.example.h_bankpro.data.network.LogoutApi
import com.example.h_bankpro.domain.useCase.LogoutUseCase
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