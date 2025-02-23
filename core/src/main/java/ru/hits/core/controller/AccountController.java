package ru.hits.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import ru.hits.core.domain.dto.account.AccountDTO;
import ru.hits.core.service.AccountService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
@Tag(name = "Счёт", description = "Контроллер, отвечающий за счета пользователей")
public class AccountController {

    private final AccountService accountService;

    @Operation(
            summary = "Создание счета",
            description = "Позволяет пользователю создать счет в системе банка"
    )
    @PostMapping
    private AccountDTO createAccount(
            @RequestParam(required = true) UUID userId
    ) {
        return accountService.createAccount(userId);
    }

    @Operation(
            summary = "Закрытие счета",
            description = "Позволяет пользователю закрыть свой счет в системе банка"
    )
    @DeleteMapping
    private AccountDTO deleteAccount(
            @RequestParam(required = true) UUID userId,
            @RequestParam(required = true) UUID accountId
    ) {
        return accountService.deleteAccount(userId, accountId);
    }

    @Operation(
            summary = "Посмотреть счета пользователя",
            description = "Позволяет пользователю посмотреть свои счета"
    )
    @GetMapping("/{userId}")
    private List<AccountDTO> getAccounts(
            @PathVariable("userId") UUID userId
    ) {
        return accountService.getAccounts(userId);
    }

}
