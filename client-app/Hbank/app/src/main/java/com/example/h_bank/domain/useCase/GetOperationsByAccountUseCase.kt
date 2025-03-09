package com.example.h_bank.domain.useCase

import com.example.h_bank.data.dto.payment.OperationShortDto
import com.example.h_bank.data.dto.PageResponse
import com.example.h_bank.data.dto.Pageable
import com.example.h_bank.data.dto.payment.toDomain
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.data.utils.mapSuccess
import com.example.h_bank.presentation.paymentHistory.model.OperationShortModel
import com.example.h_bank.domain.repository.IOperationRepository
import com.example.h_bank.domain.repository.payment.IPaymentStorageRepository

class GetOperationsByAccountUseCase(
    private val storageRepository: IPaymentStorageRepository,
    private val operationRepository: IOperationRepository
) {
    suspend operator fun invoke(
        pageable: Pageable = Pageable()
    ): RequestResult<PageResponse<OperationShortModel>> {
        val filters = storageRepository.getFilters()

        return operationRepository.getOperationsByAccount(filters, pageable)
            .mapSuccess { pageResponse: PageResponse<OperationShortDto> ->
                PageResponse(
                    content = pageResponse.content.map { it.toDomain() },
                    totalPages = pageResponse.totalPages,
                    totalElements = pageResponse.totalElements,
                    first = pageResponse.first,
                    last = pageResponse.last,
                    size = pageResponse.size,
                    number = pageResponse.number,
                    sort = pageResponse.sort,
                    pageable = pageResponse.pageable,
                    numberOfElements = pageResponse.numberOfElements,
                    empty = pageResponse.empty
                )
            }
    }
}
