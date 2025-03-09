package com.example.h_bank.domain.useCase.payment

import com.example.h_bank.data.dto.payment.OperationDetailsDto
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IOperationRepository
import com.example.h_bank.domain.repository.payment.IPaymentStorageRepository

class GetOperationDetailsUseCase(
    private val repository: IOperationRepository,
    private val storageRepository: IPaymentStorageRepository,
) {
    suspend operator fun invoke(operationId: String): RequestResult<OperationDetailsDto> {
        val accountId = storageRepository.getFilters().accountId.orEmpty()

        return repository.getOperationDetails(accountId, operationId)
    }
}