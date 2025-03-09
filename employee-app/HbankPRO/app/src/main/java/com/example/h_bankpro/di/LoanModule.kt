package com.example.h_bankpro.di

import com.example.h_bankpro.data.network.LoanApi
import com.example.h_bankpro.data.repository.LoanRepository
import com.example.h_bankpro.data.repository.LoanStorageRepository
import com.example.h_bankpro.domain.repository.ILoanRepository
import com.example.h_bankpro.domain.repository.ILoanStorageRepository
import com.example.h_bankpro.domain.useCase.CreateTariffUseCase
import com.example.h_bankpro.domain.useCase.DeleteTariffUseCase
import com.example.h_bankpro.domain.useCase.GetTariffListUseCase
import com.example.h_bankpro.domain.useCase.GetUserLoansUseCase
import com.example.h_bankpro.domain.useCase.UpdateTariffUseCase
import com.example.h_bankpro.domain.useCase.tariff.GetTariffFlowUseCase
import com.example.h_bankpro.domain.useCase.tariff.UpdateCurrentTariffUseCase
import com.example.h_bankpro.presentation.rateCreation.RateCreationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val loanModule = module {
    single<ILoanRepository> {
        LoanRepository(
            loanApi = get()
        )
    }

    factory<CreateTariffUseCase> {
        CreateTariffUseCase(loanRepository = get())
    }

    factory<GetTariffListUseCase> {
        GetTariffListUseCase(loanRepository = get())
    }

    factory<DeleteTariffUseCase> {
        DeleteTariffUseCase(loanRepository = get())
    }

    factory<UpdateTariffUseCase> {
        UpdateTariffUseCase(loanRepository = get())
    }

    factory<GetUserLoansUseCase> {
        GetUserLoansUseCase(loanRepository = get())
    }

    factory<LoanApi> {
        val retrofit = get<Retrofit>(named("loanApi"))
        retrofit.create(LoanApi::class.java)
    }

    single<ILoanStorageRepository> {
        LoanStorageRepository()
    }

    factory<GetTariffFlowUseCase> {
        GetTariffFlowUseCase(
            storageRepository = get(),
        )
    }

    factory<UpdateCurrentTariffUseCase> {
        UpdateCurrentTariffUseCase(
            storageRepository = get(),
        )
    }

    viewModel { RateCreationViewModel(get(), get(), get(), get()) }
}