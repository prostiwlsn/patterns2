package ru.hits.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.core.domain.dto.account.AccountDTO;
import ru.hits.core.domain.dto.account.AccountFilters;
import ru.hits.core.domain.entity.AccountEntity;
import ru.hits.core.exceptions.AccountNotFoundException;
import ru.hits.core.exceptions.BadRequestException;
import ru.hits.core.exceptions.ForbiddenException;
import ru.hits.core.mapper.AccountMapper;
import ru.hits.core.repository.AccountRepository;
import ru.hits.core.service.AccountService;
import ru.hits.core.specification.AccountSpecification;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    private static final Random RANDOM = new Random();

    @Transactional
    @Override
    public AccountDTO createAccount(UUID userId) {

        // TODO проверка что пользователь существует

        var accountEntity = accountRepository.save(
                AccountEntity.builder()
                        .id(UUID.randomUUID())
                        .userId(userId)
                        .accountNumber(generateAccountNumber())
                        .createDateTime(Instant.now())
                        .balance(0f)
                        .isDeleted(false)
                        .build()
        );
        return accountMapper.entityToDTO(accountEntity);
    }

    @Transactional
    @Override
    public AccountDTO deleteAccount(UUID userId, UUID accountId) {
        var accountEntity = accountRepository.findById(accountId).orElse(null);

        if (accountEntity == null) {
            throw new BadRequestException("Счёт с идентификатором " + accountId + " не найден");
        }

        if (!accountEntity.getUserId().equals(userId)) {
            throw new ForbiddenException();
        }

        accountEntity.setIsDeleted(true);
        return accountMapper.entityToDTO(accountRepository.save(accountEntity));
    }

    @Transactional(readOnly = true)
    @Override
    public List<AccountDTO> getAccounts(UUID userId) {
        AccountFilters request = AccountFilters.builder()
                .userId(userId)
                .build();
        return getAccounts(request);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AccountDTO> getAccounts(AccountFilters request) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createDateTime");
        return getAccounts(request, sort);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AccountDTO> getAccounts(AccountFilters request, Sort sort) {
        Specification<AccountEntity> spec = getSpecByFilters(request);
        return accountRepository.findAll(spec, sort)
                .stream()
                .map(accountMapper::entityToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public AccountEntity getRawAccount(UUID accountId) {
        return accountRepository.findById(accountId)
                        .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    @Override
    public void updateBalance(AccountEntity account, Float newBalance) {
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    private String generateAccountNumber() {
        StringBuilder sb = new StringBuilder(20);
        for (int i = 0; i < 20; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    private Specification<AccountEntity> getSpecByFilters(AccountFilters request) {
        return Specification.where(AccountSpecification.userIdEqual(request.getUserId()));
    }
}
