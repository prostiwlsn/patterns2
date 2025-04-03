package ru.hits.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hits.core.domain.dto.account.AccountDTO;
import ru.hits.core.domain.dto.currency.CurrencyEnum;
import ru.hits.core.domain.dto.user.UserInfoRequest;
import ru.hits.core.domain.dto.user.UserDTO;
import ru.hits.core.service.AccountService;
import ru.hits.core.service.impl.JwtService;
import ru.hits.core.service.impl.rabbitmq.rpc.UserInfoService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
@Tag(name = "Счёт", description = "Контроллер, отвечающий за счета пользователей")
public class AccountController {

    private final AccountService accountService;
    private final JwtService jwtService;

    @Operation(
            summary = "Создание счета",
            description = "Позволяет пользователю создать счет в системе банка"
    )
    @PostMapping
    private AccountDTO createAccount(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam CurrencyEnum currency
    ) throws JsonProcessingException {
        return accountService
                .createAccount(jwtService.getUserId(authHeader), currency, authHeader.substring(7));
    }

    @Operation(
            summary = "Закрытие счета",
            description = "Позволяет пользователю закрыть свой счет в системе банка"
    )
    @DeleteMapping
    private AccountDTO deleteAccount(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = true) UUID accountId
    ) {
        return accountService.deleteAccount(jwtService.getUserId(authHeader), accountId);
    }

    @Operation(
            summary = "Посмотреть счета пользователя",
            description = "Позволяет пользователю посмотреть свои счета"
    )
    @GetMapping("/{userId}/list")
    private List<AccountDTO> getAccounts(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("userId") UUID userId,
            @RequestParam(required = false) Boolean isDeleted
    ) throws JsonProcessingException {
        return accountService
                .getAccounts(jwtService.getUserId(authHeader), userId, isDeleted, authHeader.substring(7));
    }

    @Operation(
            summary = "Получить идентификатор счета по номеру",
            description = "Позволяет пользователю получить идентификатор счета по номеру"
    )
    @GetMapping("/{accountNumber}")
    private UUID getAccountId(
            @PathVariable("accountNumber") String accountNumber
    ) {
        return accountService.getAccountId(accountNumber);
    }

}
