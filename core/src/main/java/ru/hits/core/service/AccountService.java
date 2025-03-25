package ru.hits.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Sort;
import ru.hits.core.domain.dto.account.AccountDTO;
import ru.hits.core.domain.dto.account.AccountFilters;
import ru.hits.core.domain.dto.currency.CurrencyEnum;
import ru.hits.core.domain.entity.AccountEntity;

import java.util.List;
import java.util.UUID;

public interface AccountService {

    AccountDTO createAccount(UUID userId, CurrencyEnum currency) throws JsonProcessingException;

    AccountDTO deleteAccount(UUID userId, UUID accountId);

    List<AccountDTO> getAccounts(UUID myUserId, UUID userId, Boolean isDeleted) throws JsonProcessingException;

    List<AccountDTO> getAccounts(AccountFilters request);

    List<AccountDTO> getAccounts(AccountFilters request, Sort sort);

    AccountEntity getRawAccount(UUID accountId);

    List<UUID> getMyAccountIds(UUID userId);

    void updateBalance(AccountEntity account, Float newBalance);

    UUID getAccountId(String accountNumber);

}
