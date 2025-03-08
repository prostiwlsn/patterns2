package com.example.h_bankpro.di

import com.example.h_bankpro.data.network.TokenApi
import com.example.h_bankpro.data.repository.TokenRepository
import com.example.h_bankpro.domain.repository.ITokenRepository
import com.example.h_bankpro.domain.useCase.RefreshTokenUseCase
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