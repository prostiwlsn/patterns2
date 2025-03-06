package com.example.h_bankpro.di

import com.example.h_bankpro.data.network.OperationApi
import com.example.h_bankpro.data.repository.OperationRepository
import com.example.h_bankpro.domain.repository.IOperationRepository
import com.example.h_bankpro.domain.useCase.GetOperationInfoUseCase
import com.example.h_bankpro.domain.useCase.GetOperationsByAccountUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val operationModule = module {
    single<IOperationRepository> {
        OperationRepository(
            api = get()
        )
    }

    factory<GetOperationInfoUseCase> {
        GetOperationInfoUseCase(
            operationRepository = get()
        )
    }

    factory<GetOperationsByAccountUseCase> {
        GetOperationsByAccountUseCase(
            operationRepository = get()
        )
    }

    factory<OperationApi> {
        val retrofit = get<Retrofit>(named("yuraApi"))
        retrofit.create(OperationApi::class.java)
    }
}