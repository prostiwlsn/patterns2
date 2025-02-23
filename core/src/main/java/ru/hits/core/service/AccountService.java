package ru.hits.core.service;

import org.springframework.data.domain.Sort;
import ru.hits.core.domain.dto.account.AccountDTO;
import ru.hits.core.domain.dto.account.AccountFilters;
import ru.hits.core.domain.entity.AccountEntity;

import java.util.List;
import java.util.UUID;

public interface AccountService {

    AccountDTO createAccount(UUID userId);

    AccountDTO deleteAccount(UUID userId, UUID accountId);

    List<AccountDTO> getAccounts(UUID userId);

    List<AccountDTO> getAccounts(AccountFilters request);

    List<AccountDTO> getAccounts(AccountFilters request, Sort sort);

    AccountEntity getRawAccount(UUID accountId);

    void updateBalance(AccountEntity account, Float newBalance);

}
