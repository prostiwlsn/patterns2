package com.example.h_bank.di

import com.example.h_bank.data.network.AccountApi
import com.example.h_bank.data.repository.AccountRepository
import com.example.h_bank.domain.repository.IAccountRepository
import com.example.h_bank.domain.useCase.CloseAccountUseCase
import com.example.h_bank.domain.useCase.GetUserAccountsUseCase
import com.example.h_bank.domain.useCase.OpenAccountUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val accountModule = module {
    single<IAccountRepository> {
        AccountRepository(
            api = get()
        )
    }

    factory<GetUserAccountsUseCase> {
        GetUserAccountsUseCase(
            accountRepository = get()
        )
    }

    factory<OpenAccountUseCase> {
        OpenAccountUseCase(
            accountRepository = get()
        )
    }

    factory<CloseAccountUseCase> {
        CloseAccountUseCase(
            accountRepository = get()
        )
    }

    factory<AccountApi> {
        val retrofit = get<Retrofit>(named("accountApi"))
        retrofit.create(AccountApi::class.java)
    }
}