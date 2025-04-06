package ru.hits.core.domain.dto.operation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledPayDTO {
    private UUID accountId;
    private UUID loanId;
    private Float amount;
}
