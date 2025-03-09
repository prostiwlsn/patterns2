package com.example.h_bank.domain.useCase.loan

import com.example.h_bank.domain.repository.loan.ILoanStorageRepository
import com.example.h_bank.presentation.loanProcessing.model.LoanProcessingErrorModel

class LoanProcessingValidationUseCase(
    private val storageRepository: ILoanStorageRepository
) {
    operator fun invoke(): LoanProcessingErrorModel? {
        val formFields = storageRepository.getLoanState()

        return LoanProcessingErrorModel(
            durationError = validateDuration(formFields.duration),
            amountError = validateAmount(formFields.amount),
        ).takeIf { it != LoanProcessingErrorModel() }
    }

    private fun validateDuration(durationField: Int?): String? {
        return when {
            durationField == null -> "Заполните поле"
            durationField > 5 -> "Срок должен быть не выше 5 лет"
            else -> null
        }
    }

    private fun validateAmount(durationField: Int?): String? {
        return when {
            durationField == null -> "Заполните поле"
            durationField > 10_000_000 -> "Сумма кредита не может быть выше 10000000"
            else -> null
        }
    }
}