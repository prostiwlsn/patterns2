package ru.hits.core.domain.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AccountFilters {

    @Schema(name = "Идентификатор пользователя")
    private UUID userId;

}