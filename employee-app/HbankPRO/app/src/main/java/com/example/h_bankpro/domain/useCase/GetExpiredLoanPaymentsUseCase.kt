package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.data.dto.OperationShortDto
import com.example.h_bankpro.data.dto.PageResponse
import com.example.h_bankpro.data.dto.Pageable
import com.example.h_bankpro.data.dto.toDomain
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.data.utils.mapSuccess
import com.example.h_bankpro.domain.model.OperationShort
import com.example.h_bankpro.domain.repository.IOperationRepository

class GetExpiredLoanPaymentsUseCase(
    private val operationRepository: IOperationRepository
) {
    suspend operator fun invoke(
        loanId: String,
        userId: String,
        pageable: Pageable = Pageable()
    ): RequestResult<PageResponse<OperationShort>> {
        return operationRepository.getExpiredLoanPayments(
            loanId,
            userId,
            pageable
        ).mapSuccess { pageResponse: PageResponse<OperationShortDto> ->
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