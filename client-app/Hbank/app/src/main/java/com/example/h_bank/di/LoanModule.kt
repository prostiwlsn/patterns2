package com.example.h_bank.di

import com.example.h_bank.data.network.LoanApi
import com.example.h_bank.data.repository.loan.LoanRemoteRepository
import com.example.h_bank.data.repository.loan.LoanStorageRepository
import com.example.h_bank.domain.repository.loan.ILoanRemoteRepository
import com.example.h_bank.domain.repository.loan.ILoanStorageRepository
import com.example.h_bank.domain.useCase.loan.GetLoanFlowUseCase
import com.example.h_bank.domain.useCase.loan.GetTariffListUseCase
import com.example.h_bank.domain.useCase.loan.LoanProcessingValidationUseCase
import com.example.h_bank.domain.useCase.loan.UpdateLoanUseCase
import com.example.h_bank.presentation.loanProcessing.LoanProcessingViewModel
import com.example.h_bank.presentation.loanProcessing.model.LoanProcessingErrorModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val loanModule = module {
    single<ILoanStorageRepository> {
        LoanStorageRepository()
    }

    factory<UpdateLoanUseCase> {
        UpdateLoanUseCase(
            loanStorageRepository = get()
        )
    }

    factory<GetLoanFlowUseCase> {
        GetLoanFlowUseCase(
            storageRepository = get()
        )
    }

    factory<LoanProcessingValidationUseCase> {
        LoanProcessingValidationUseCase(
            storageRepository = get()
        )
    }

    factory<LoanApi> {
        val retrofit = get<Retrofit>(named("loanApi"))

        retrofit.create(LoanApi::class.java)
    }

    factory<ILoanRemoteRepository> {
        LoanRemoteRepository(
            loanApi = get()
        )
    }

    factory<GetTariffListUseCase> {
        GetTariffListUseCase(
            loanRepository = get()
        )
    }

    viewModel {
        LoanProcessingViewModel(
            updateLoanUseCase = get(),
            getLoanFlowUseCase = get(),
            loanProcessingValidationUseCase = get(),
        )
    }
}