package ru.hits.core.domain.dto.loan;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LoanRequest {

    private UUID accountId;
    private float amount;

}
