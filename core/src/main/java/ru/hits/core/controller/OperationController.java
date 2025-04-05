package ru.hits.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.hits.core.domain.dto.operation.OperationDTO;
import ru.hits.core.domain.dto.operation.OperationShortDTO;
import ru.hits.core.domain.dto.operation.OperationRequestBody;
import ru.hits.core.domain.enums.OperationTypeEnum;
import ru.hits.core.service.OperationService;
import ru.hits.core.service.impl.JwtService;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/operation")
@Tag(name = "Операции счёта", description = "Контроллер, отвечающий за операции счёта")
public class OperationController {

    private final OperationService operationService;
    private final JwtService jwtService;

    @Operation(
            summary = "Создание операции",
            description = "Позволяет пользователю перевести деньги, пополнить счет, вывести деньги"
    )
    @PostMapping
    private OperationDTO createOperation(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody(required = true) OperationRequestBody operationRequestBody
    ) throws JsonProcessingException {
        return operationService.sendCreateOperationMessage(authHeader, jwtService.getUserId(authHeader), operationRequestBody);
    }

    @Operation(
            summary = "История операций по счёту",
            description = "Позволяет пользователю посмотреть историю операций по счету"
    )
    @GetMapping("/byAccount/{accountId}")
    private Page<OperationShortDTO> getOperations(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("accountId") UUID accountId,
            @RequestParam(required = false) Instant timeStart,
            @RequestParam(required = false) Instant timeEnd,
            @RequestParam(required = false) OperationTypeEnum operationType,
            @PageableDefault(size = 10, page = 0, sort = "transactionDateTime", direction = Sort.Direction.DESC)
            Pageable pageable
    ) throws JsonProcessingException {
        return operationService.getOperations(
                jwtService.getUserId(authHeader),
                accountId,
                timeStart,
                timeEnd,
                operationType,
                pageable,
                authHeader.substring(7)
        );
    }

    @Operation(
            summary = "История просроченных платежей",
            description = "Позволяет пользователю посмотреть историю операций по счету"
    )
    @GetMapping("/expiredLoanPayment")
    private Page<OperationShortDTO> getExpiredOperations(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("loanAccountId") UUID loanAccountId,
            @PageableDefault(size = 10, page = 0, sort = "transactionDateTime", direction = Sort.Direction.DESC)
            Pageable pageable
    ) throws JsonProcessingException {
        return operationService.getExpiredOperations(
                jwtService.getUserId(authHeader),
                loanAccountId,
                pageable,
                authHeader.substring(7)
        );
    }

    @Operation(
            summary = "Полная операция по счету",
            description = "Позволяет пользователю посмотреть полную операцию по счету"
    )
    @GetMapping("/{accountId}/{operationId}")
    private OperationDTO getOperation(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("accountId") UUID accountId,
            @PathVariable("operationId") UUID operationId
    ) throws JsonProcessingException {
        return operationService
                .getOperation(
                        jwtService.getUserId(authHeader), accountId, operationId, authHeader.substring(7)
                );
    }

}
