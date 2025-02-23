package ru.hits.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.hits.core.domain.dto.operation.OperationDTO;
import ru.hits.core.domain.dto.operation.OperationShortDTO;
import ru.hits.core.domain.dto.operation.OperationRequestBody;
import ru.hits.core.service.OperationService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/operation")
@Tag(name = "Операции счёта", description = "Контроллер, отвечающий за операции счёта")
public class OperationController {

    private final OperationService operationService;

    @Operation(
            summary = "Создание операции",
            description = "Позволяет пользователю перевести деньги, пополнить счет, вывести деньги"
    )
    @PostMapping
    private OperationDTO createOperation(
            @RequestBody(required = true) OperationRequestBody operationRequestBody
    ) {
        return operationService.createOperation(operationRequestBody);
    }

    @Operation(
            summary = "История операций по счёту",
            description = "Позволяет пользователю посмотреть историю операций по счету"
    )
    @GetMapping("/byAccount/{accountId}")
    private Page<OperationShortDTO> getOperations(
            @PathVariable("accountId") UUID accountId,
            @PageableDefault(size = 10, page = 0, sort = "transactionDateTime", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return operationService.getOperations(accountId, pageable);
    }

    @Operation(
            summary = "Полная операция по счету",
            description = "Позволяет пользователю посмотреть полную операцию по счету"
    )
    @GetMapping("/{operationId}")
    private OperationDTO getOperation(
            @PathVariable("operationId") UUID operationId
    ) {
        return operationService.getOperation(operationId);
    }

}
