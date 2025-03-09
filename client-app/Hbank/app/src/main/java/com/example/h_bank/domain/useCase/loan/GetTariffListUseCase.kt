package com.example.h_bank.domain.useCase.loan

import com.example.h_bank.data.dto.Pageable
import com.example.h_bank.data.dto.loan.toDomain
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.data.utils.mapSuccess
import com.example.h_bank.domain.entity.loan.LoansPageResponse
import com.example.h_bank.domain.entity.loan.TariffEntity
import com.example.h_bank.domain.repository.loan.ILoanRemoteRepository

class GetTariffListUseCase(
    private val loanRepository: ILoanRemoteRepository
) {
    suspend operator fun invoke(
        pageable: Pageable,
    ): RequestResult<LoansPageResponse<TariffEntity>> {
        return loanRepository.getTariffList(pageable.page, pageable.size)
            .mapSuccess { it.toDomain() }
    }
}