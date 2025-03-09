package com.example.h_bank.di

import com.example.h_bank.data.repository.authorization.AuthorizationCommandStorage
import com.example.h_bank.data.repository.payment.PaymentStorageRepository
import com.example.h_bank.domain.repository.authorization.IAuthorizationCommandStorage
import com.example.h_bank.domain.repository.payment.IPaymentStorageRepository
import com.example.h_bank.domain.useCase.authorization.GetMainCommandsUseCase
import com.example.h_bank.domain.useCase.authorization.PushCommandUseCase
import com.example.h_bank.domain.useCase.filter.GetOperationsFiltersFlowUseCase
import com.example.h_bank.domain.useCase.filter.UpdateOperationsFilterUseCase
import com.example.h_bank.domain.useCase.payment.GetOperationDetailsUseCase
import com.example.h_bank.domain.useCase.payment.GetOperationsHistoryUseCase
import com.example.h_bank.presentation.paymentHistory.PaymentHistoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val paymentModule = module {
    single<IPaymentStorageRepository> {
        PaymentStorageRepository()
    }

    factory<GetOperationsFiltersFlowUseCase> {
        GetOperationsFiltersFlowUseCase(
            storageRepository = get()
        )
    }

    factory<UpdateOperationsFilterUseCase> {
        UpdateOperationsFilterUseCase(
            storageRepository = get()
        )
    }

    factory<GetOperationsHistoryUseCase> {
        GetOperationsHistoryUseCase(
            storageRepository = get(),
            operationRepository = get(),
        )
    }

    factory<GetOperationsFiltersFlowUseCase> {
        GetOperationsFiltersFlowUseCase(
            storageRepository = get(),
        )
    }

    single<IAuthorizationCommandStorage> {
        AuthorizationCommandStorage()
    }

    factory<PushCommandUseCase> {
        PushCommandUseCase(
            storage = get()
        )
    }

    factory<GetMainCommandsUseCase> {
        GetMainCommandsUseCase(
            storage = get()
        )
    }

    factory<GetOperationDetailsUseCase> {
        GetOperationDetailsUseCase(
            storageRepository = get(),
            repository = get(),
        )
    }

    viewModel {
        PaymentHistoryViewModel(
            getUserIdUseCase = get(),
            getUserAccountsUseCase = get(),
            updateOperationsFilterUseCase = get(),
            getOperationsHistoryUseCase = get(),
            getOperationsFiltersFlowUseCase = get(),
            pushCommandUseCase = get(),
        )
    }
}