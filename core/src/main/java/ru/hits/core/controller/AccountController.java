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

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
@Tag(name = "Счёт", description = "Контроллер, отвечающий за счета пользователей")
public class AccountController {

    private final AccountService accountService;
    private final JwtService jwtService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Operation(
            summary = "Создание счета",
            description = "Позволяет пользователю создать счет в системе банка"
    )
    @PostMapping
    public AccountDTO createAccount(
            @RequestHeader("Authorization") String authHeader,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @RequestParam CurrencyEnum currency
    ) throws JsonProcessingException {
        if (idempotencyKey == null || idempotencyKey.isEmpty()) {
            return accountService.createAccount(
                    jwtService.getUserId(authHeader),
                    currency,
                    authHeader.substring(7)
            );
        }

        String cacheKey = "account:create:" + idempotencyKey;
        AccountDTO cachedResult = (AccountDTO) redisTemplate.opsForValue().get(cacheKey);

        if (cachedResult != null) {
            return cachedResult;
        }

        AccountDTO result = accountService.createAccount(
                jwtService.getUserId(authHeader),
                currency,
                authHeader.substring(7)
        );

        redisTemplate.opsForValue().set(cacheKey, result, Duration.ofHours(24));
        return result;
    }

    @Operation(
            summary = "Закрытие счета",
            description = "Позволяет пользователю закрыть свой счет в системе банка"
    )
    @DeleteMapping
    public AccountDTO deleteAccount(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam UUID accountId,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey
    ) {
        if (idempotencyKey != null && !idempotencyKey.isEmpty()) {
            String cacheKey = "account:delete:" + idempotencyKey;
            AccountDTO cachedResult = (AccountDTO) redisTemplate.opsForValue().get(cacheKey);

            if (cachedResult != null) {
                return cachedResult;
            }

            AccountDTO result = accountService.deleteAccount(
                    jwtService.getUserId(authHeader),
                    accountId
            );

            redisTemplate.opsForValue().set(cacheKey, result, Duration.ofHours(24));
            return result;
        }

        return accountService.deleteAccount(jwtService.getUserId(authHeader), accountId);
    }

    @Operation(
            summary = "Посмотреть счета пользователя",
            description = "Позволяет пользователю посмотреть свои счета"
    )
    @GetMapping("/{userId}/list")
    public List<AccountDTO> getAccounts(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("userId") UUID userId,
            @RequestParam(required = false) Boolean isDeleted
    ) throws JsonProcessingException {
        return accountService.getAccounts(
                jwtService.getUserId(authHeader),
                userId,
                isDeleted,
                authHeader.substring(7)
        );
    }

    @Operation(
            summary = "Получить идентификатор счета по номеру",
            description = "Позволяет пользователю получить идентификатор счета по номеру"
    )
    @GetMapping("/{accountNumber}")
    public UUID getAccountId(
            @PathVariable("accountNumber") String accountNumber
    ) {
        return accountService.getAccountId(accountNumber);
    }
}