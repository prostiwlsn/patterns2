package ru.hits.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hits.core.domain.dto.account.AccountDTO;
import ru.hits.core.domain.dto.user.RpcRequest;
import ru.hits.core.domain.dto.user.UserDTO;
import ru.hits.core.rpc.RpcClientService;
import ru.hits.core.service.AccountService;
import ru.hits.core.service.impl.JwtService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
@Tag(name = "Счёт", description = "Контроллер, отвечающий за счета пользователей")
public class AccountController {

    private final AccountService accountService;
    private final JwtService jwtService;
    private final RpcClientService rpcClientService;

    @Operation(
            summary = "Тест для получения профиля"
    )
    @GetMapping("/test")
    public UserDTO test(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Invalid Authorization header");
            }

            var id = jwtService.getUserId(authHeader);
            return rpcClientService.sendRequest(new RpcRequest(id));

        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }
    }

    @Operation(
            summary = "Создание счета",
            description = "Позволяет пользователю создать счет в системе банка"
    )
    @PostMapping
    private AccountDTO createAccount(
            @RequestHeader("Authorization") String authHeader
    ) {
        return accountService.createAccount(jwtService.getUserId(authHeader));
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
            @PathVariable("userId") UUID userId
    ) throws JsonProcessingException {
        return accountService.getAccounts(jwtService.getUserId(authHeader), userId);
    }

}
