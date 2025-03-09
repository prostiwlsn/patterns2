package ru.hits.core.domain.dto.operation;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LoanPaymentErrorResponse {

    private UUID senderAccountId;
    private float amount;
    private String errorMessage;
    private String errorStatusCode;

}
