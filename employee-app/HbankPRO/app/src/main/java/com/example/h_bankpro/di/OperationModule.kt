package com.example.h_bankpro.di

import com.example.h_bankpro.data.dataSource.remote.OperationRemoteDataSource
import com.example.h_bankpro.data.network.OperationApi
import com.example.h_bankpro.data.network.OperationWebSocketApi
import com.example.h_bankpro.data.network.OperationWebSocketClient
import com.example.h_bankpro.data.repository.OperationRepository
import com.example.h_bankpro.domain.repository.IOperationRepository
import com.example.h_bankpro.domain.useCase.GetExpiredLoanPaymentsUseCase
import com.example.h_bankpro.domain.useCase.GetOperationInfoUseCase
import com.example.h_bankpro.domain.useCase.GetOperationsByAccountUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val operationModule = module {
    single { OperationRemoteDataSource(api = get(), webSocketApi = get()) }

    single<IOperationRepository> {
        OperationRepository(
            remoteDataSource = get()
        )
    }

    factory<GetExpiredLoanPaymentsUseCase> {
        GetExpiredLoanPaymentsUseCase(
            operationRepository = get()
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
        val retrofit = get<Retrofit>(named("accountApi"))
        retrofit.create(OperationApi::class.java)
    }

    single<OperationWebSocketApi> {
        OperationWebSocketClient(client = get(named("authClient")))
    }
}