package com.example.h_bank.di

import com.example.h_bank.data.network.TokenApi
import com.example.h_bank.data.repository.TokenRepository
import com.example.h_bank.domain.repository.ITokenRepository
import com.example.h_bank.domain.useCase.RefreshTokenUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val tokenModule = module {
    factory<RefreshTokenUseCase> {
        RefreshTokenUseCase(
            localRepository = get(),
            tokenRepository = get()
        )
    }

    single<ITokenRepository> {
        TokenRepository(
            localRepository = get(),
            tokenApi = get()
        )
    }

    factory<TokenApi> {
        val retrofit = get<Retrofit>(named("infoAuthApi"))
        retrofit.create(TokenApi::class.java)
    }
}