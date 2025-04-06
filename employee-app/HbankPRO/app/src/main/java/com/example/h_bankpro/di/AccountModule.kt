package com.example.h_bankpro.di

import com.example.h_bankpro.data.dataSource.remote.AccountRemoteDataSource
import com.example.h_bankpro.data.network.AccountApi
import com.example.h_bankpro.data.repository.AccountRepository
import com.example.h_bankpro.domain.repository.IAccountRepository
import com.example.h_bankpro.domain.useCase.GetUserAccountsUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val accountModule = module {
    single { AccountRemoteDataSource(api = get()) }
    single<IAccountRepository> {
        AccountRepository(
            remoteDataSource = get()
        )
    }

    factory<GetUserAccountsUseCase> {
        GetUserAccountsUseCase(
            accountRepository = get()
        )
    }

    factory<AccountApi> {
        val retrofit = get<Retrofit>(named("accountApi"))
        retrofit.create(AccountApi::class.java)
    }
}