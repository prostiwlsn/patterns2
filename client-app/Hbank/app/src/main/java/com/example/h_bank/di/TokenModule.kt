package com.example.h_bank.di

import com.example.h_bank.data.dataSource.remote.TokenRemoteDataSource
import com.example.h_bank.data.network.TokenApi
import com.example.h_bank.data.repository.TokenRepository
import com.example.h_bank.domain.repository.authorization.IAuthorizationLocalRepository
import com.example.h_bank.domain.repository.authorization.ITokenRepository
import com.example.h_bank.domain.useCase.authorization.RefreshTokenUseCase
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
            localRepository = get<IAuthorizationLocalRepository>(),
            remoteDataSource = get()
        )
    }

    single { TokenRemoteDataSource(tokenApi = get(), localRepository = get<IAuthorizationLocalRepository>()) }

    factory<TokenApi> {
        val retrofit = get<Retrofit>(named("infoAuthApi"))
        retrofit.create(TokenApi::class.java)
    }
}