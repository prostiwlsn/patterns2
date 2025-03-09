package ru.hits.core.domain.dto.operation;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Nonnull
public class LoanPaymentRequest {

    private UUID SenderAccountId;
    private UUID RecipientAccountId;
    private float Amount;

}
