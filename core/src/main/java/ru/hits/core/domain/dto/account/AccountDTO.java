package ru.hits.core.domain.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.Builder;
import lombok.Data;
import ru.hits.core.domain.dto.currency.CurrencyEnum;
import ru.hits.core.utils.CurrencyEnumConverter;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@Schema(description = "DTO, представляющий счёт пользователя")
public class AccountDTO {

    @Schema(description = "Уникальный идентификатор счёта", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Уникальный номер счёта", example = "2202 1685 1423 4312")
    private String accountNumber;

    @Schema(description = "Баланс счёта", example = "1000.50")
    private Float balance;

    @Schema(description = "валюта", example = "AMD")
    private CurrencyEnum currency;

    @Schema(description = "Идентификатор пользователя, которому принадлежит счёт",
            example = "e0d1234c-7b4a-4b4f-9c1c-1e27b1f7f3af")
    private UUID userId;

    @Schema(description = "Флаг, показывающий, удалён ли счёт", example = "false")
    private Boolean isDeleted;

    @Schema(description = "Дата и время создания счёта", example = "2024-02-22T14:30:00Z")
    private Instant createDateTime;

}
