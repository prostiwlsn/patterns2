package com.example.h_bank.di

import com.example.h_bank.data.network.OperationApi
import com.example.h_bank.data.repository.payment.OperationRepository
import com.example.h_bank.domain.repository.IOperationRepository
import com.example.h_bank.domain.useCase.GetOperationInfoUseCase
import com.example.h_bank.domain.useCase.GetOperationsByAccountUseCase
import com.example.h_bank.domain.useCase.RepayLoanUseCase
import com.example.h_bank.domain.useCase.ReplenishUseCase
import com.example.h_bank.domain.useCase.TransferUseCase
import com.example.h_bank.domain.useCase.WithdrawUseCase
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
            storageRepository = get(),
            operationRepository = get()
        )
    }

    factory<TransferUseCase> {
        TransferUseCase(
            operationRepository = get()
        )
    }

    factory<ReplenishUseCase> {
        ReplenishUseCase(
            operationRepository = get()
        )
    }

    factory<WithdrawUseCase> {
        WithdrawUseCase(
            operationRepository = get()
        )
    }

    factory<RepayLoanUseCase> {
        RepayLoanUseCase(
            operationRepository = get()
        )
    }

    factory<OperationApi> {
        val retrofit = get<Retrofit>(named("accountApi"))
        retrofit.create(OperationApi::class.java)
    }
}