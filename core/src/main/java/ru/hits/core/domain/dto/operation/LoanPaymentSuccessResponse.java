package ru.hits.core.domain.dto.operation;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LoanPaymentSuccessResponse {

    private UUID senderAccountId;
    private UUID recipientAccountId;
    private float amount;

}
