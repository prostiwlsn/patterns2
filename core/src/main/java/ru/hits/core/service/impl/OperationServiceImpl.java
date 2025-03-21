package ru.hits.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.core.domain.dto.operation.*;
import ru.hits.core.domain.enums.RoleEnum;
import ru.hits.core.domain.dto.user.UserInfoRequest;
import ru.hits.core.domain.entity.AccountEntity;
import ru.hits.core.domain.entity.OperationEntity;
import ru.hits.core.domain.enums.OperationTypeEnum;
import ru.hits.core.exceptions.BadRequestException;
import ru.hits.core.exceptions.ForbiddenException;
import ru.hits.core.exceptions.OperationNotFoundException;
import ru.hits.core.mapper.OperationMapper;
import ru.hits.core.repository.OperationRepository;
import ru.hits.core.service.AccountService;
import ru.hits.core.service.OperationService;
import ru.hits.core.specification.OperationSpecification;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {

    private final float MAX_MONEY_VALUE = 100000000f;

    private final AccountService accountService;
    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;

    private final RpcClientService rpcClientService;

    @Transactional
    @Override
    public OperationDTO createOperation(UUID userId, OperationRequestBody operationRequestBody) throws JsonProcessingException {
        if (MAX_MONEY_VALUE < operationRequestBody.getAmount()) {
            throw new BadRequestException("Превышено максимальное значение деняг");
        }

        switch (operationRequestBody.getOperationType()) {
            case OperationTypeEnum.TRANSFER -> {
                return createTransferOperation(userId, operationRequestBody);
            }
            case OperationTypeEnum.LOAN_REPAYMENT -> {
                return createLoanPaymentOperation(userId, operationRequestBody);
            }
            case OperationTypeEnum.REPLENISHMENT -> {
                return createReplenishmentOperation(operationRequestBody);
            }
            case OperationTypeEnum.WITHDRAWAL -> {
                return createWithdrawalOperation(userId, operationRequestBody);
            }
            default -> throw new BadRequestException(
                    "Такой операции не существует " + operationRequestBody.getOperationType()
            );
        }
    }

    private OperationDTO createTransferOperation(UUID userId, OperationRequestBody operationRequestBody) {
        if (operationRequestBody.getSenderAccountId().equals(operationRequestBody.getRecipientAccountId())) {
            throw new BadRequestException("Нельзя перевести самому себе");
        }

        var senderAccount = accountService.getRawAccount(operationRequestBody.getSenderAccountId());
        var recipientAccount = accountService.getRawAccount(operationRequestBody.getRecipientAccountId());

        if (!senderAccount.getUserId().equals(userId)) {
            throw new ForbiddenException();
        }

        if (senderAccount.getBalance() < operationRequestBody.getAmount()) {
            throw new BadRequestException("Недостаточно средств на счете отправителя.");
        }

        accountService.updateBalance(
                senderAccount,
                senderAccount.getBalance() - operationRequestBody.getAmount()
        );
        accountService.updateBalance(
                recipientAccount,
                recipientAccount.getBalance() + operationRequestBody.getAmount()
        );

        if (MAX_MONEY_VALUE < recipientAccount.getBalance()) {
            throw new BadRequestException("Превышено максимальное значение деняг");
        }

        var operation = OperationEntity.builder()
                .id(UUID.randomUUID())
                .senderAccountId(operationRequestBody.getSenderAccountId())
                .recipientAccountId(operationRequestBody.getRecipientAccountId())
                .amount(operationRequestBody.getAmount())
                .transactionDateTime(Instant.now())
                .message(operationRequestBody.getMessage())
                .operationType(operationRequestBody.getOperationType())
                .build();

        return operationMapper.entityToDTO(
                operationRepository.save(operation),
                List.of(operationRequestBody.getSenderAccountId()),
                senderAccount.getAccountNumber(),
                recipientAccount.getAccountNumber()
        );
    }

    private OperationDTO createLoanPaymentOperation(UUID userId, OperationRequestBody operationRequestBody) throws JsonProcessingException {
        var senderAccount = accountService.getRawAccount(operationRequestBody.getSenderAccountId());

        if (!senderAccount.getUserId().equals(userId)) {
            throw new ForbiddenException();
        }

        if (senderAccount.getBalance() < operationRequestBody.getAmount()) {
            throw new BadRequestException("Недостаточно средств на счете отправителя.");
        }

        if (MAX_MONEY_VALUE < operationRequestBody.getAmount()) {
            throw new BadRequestException("Превышено максимальное значение деняг");
        }

        rpcClientService.loanPaymentRequest(
                new LoanPaymentRequest(
                        senderAccount.getId(),
                        operationRequestBody.getRecipientAccountId(),
                        operationRequestBody.getAmount()
                )
        );

        accountService.updateBalance(
                senderAccount,
                senderAccount.getBalance() - operationRequestBody.getAmount()
        );

        var operation = OperationEntity.builder()
                .id(UUID.randomUUID())
                .senderAccountId(operationRequestBody.getSenderAccountId())
                .amount(operationRequestBody.getAmount())
                .transactionDateTime(Instant.now())
                .message(operationRequestBody.getMessage())
                .operationType(operationRequestBody.getOperationType())
                .build();

        return operationMapper.entityToDTO(
                operationRepository.save(operation),
                List.of(operationRequestBody.getSenderAccountId()),
                senderAccount.getAccountNumber(),
                null
        );
    }

    private OperationDTO createReplenishmentOperation(OperationRequestBody operationRequestBody) {
        var recipientAccount = accountService.getRawAccount(operationRequestBody.getRecipientAccountId());

        if (MAX_MONEY_VALUE < operationRequestBody.getAmount()) {
            throw new BadRequestException("Превышено максимальное значение деняг");
        }

        accountService.updateBalance(
                recipientAccount,
                recipientAccount.getBalance() + operationRequestBody.getAmount()
        );

        if (MAX_MONEY_VALUE < recipientAccount.getBalance()) {
            throw new BadRequestException("Превышено максимальное значение деняг на счету");
        }

        var operation = OperationEntity.builder()
                .id(UUID.randomUUID())
                .recipientAccountId(operationRequestBody.getRecipientAccountId())
                .amount(operationRequestBody.getAmount())
                .transactionDateTime(Instant.now())
                .message(operationRequestBody.getMessage())
                .operationType(operationRequestBody.getOperationType())
                .build();

        return operationMapper.entityToDTO(
                operationRepository.save(operation),
                null,
                null,
                recipientAccount.getAccountNumber()
        );
    }

    private OperationDTO createWithdrawalOperation(UUID userId, OperationRequestBody operationRequestBody) {
        var senderAccount = accountService.getRawAccount(operationRequestBody.getSenderAccountId());

        if (!senderAccount.getUserId().equals(userId)) {
            throw new ForbiddenException();
        }

        if (senderAccount.getBalance() < operationRequestBody.getAmount()) {
            throw new BadRequestException("Недостаточно средств на счете отправителя.");
        }

        accountService.updateBalance(
                senderAccount,
                senderAccount.getBalance() - operationRequestBody.getAmount()
        );

        var operation = OperationEntity.builder()
                .id(UUID.randomUUID())
                .senderAccountId(operationRequestBody.getSenderAccountId())
                .amount(operationRequestBody.getAmount())
                .transactionDateTime(Instant.now())
                .message(operationRequestBody.getMessage())
                .operationType(operationRequestBody.getOperationType())
                .build();

        return operationMapper.entityToDTO(
                operationRepository.save(operation),
                List.of(operationRequestBody.getSenderAccountId()),
                senderAccount.getAccountNumber(),
                null
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OperationShortDTO> getOperations(
            UUID userId,
            UUID accountId,
            Instant timeStart,
            Instant timeEnd,
            OperationTypeEnum operationType,
            Pageable pageable
    ) throws JsonProcessingException {
        var user = rpcClientService.getUserInfo(
                new UserInfoRequest(userId)
        );
        var account = accountService.getRawAccount(accountId);

        if (
                user.getRoles().stream().noneMatch(r -> r.equals(RoleEnum.ADMIN) || r.equals(RoleEnum.MANAGER))
                        && !account.getUserId().equals(userId)
        ) {
            throw new ForbiddenException();
        }

        return getOperations(
                OperationFilters.builder()
                        .accountId(accountId)
                        .timeStart(timeStart)
                        .timeEnd(timeEnd)
                        .operationType(operationType)
                        .build(),
                pageable,
                List.of(accountId)
        );
    }

    private Page<OperationShortDTO> getOperations(OperationFilters filters, Pageable pageable, List<UUID> accountIds) {
        var spec = getSpecByFilters(filters);
        var test = operationRepository.findAll(spec, pageable).map(o -> operationMapper.entityToShortDTO(o, accountIds));
        return test;
    }

    @Transactional(readOnly = true)
    @Override
    public OperationDTO getOperation(
            UUID userId,
            UUID accountId,
            UUID operationId
    ) throws JsonProcessingException {
        List<UUID> accountIds = List.of(accountId);

        OperationEntity operation = operationRepository.findById(operationId)
                        .orElseThrow(() -> new OperationNotFoundException(operationId));

        var user = rpcClientService.getUserInfo(
                new UserInfoRequest(userId)
        );

        AccountEntity recipientAccount = null;
        if (!operation.getOperationType().equals(OperationTypeEnum.LOAN_REPAYMENT)) {
            recipientAccount = (operation.getRecipientAccountId() != null)
                    ? accountService.getRawAccount(operation.getRecipientAccountId())
                    : null;
        }

        AccountEntity senderAccount = (operation.getSenderAccountId() != null)
                ? accountService.getRawAccount(operation.getSenderAccountId())
                : null;

        boolean isAdminOrManager = user.getRoles().stream()
                .anyMatch(r -> r.equals(RoleEnum.ADMIN) || r.equals(RoleEnum.MANAGER));

        boolean isRecipientUser = (recipientAccount != null && recipientAccount.getUserId().equals(userId));
        boolean isSenderUser = (senderAccount != null && senderAccount.getUserId().equals(userId));

        if (!isAdminOrManager && !isRecipientUser && !isSenderUser) {
            throw new ForbiddenException();
        }

        return operationMapper.entityToDTO(
                operation,
                accountIds,
                senderAccount == null ? null : senderAccount.getAccountNumber(),
                recipientAccount == null ? null : recipientAccount.getAccountNumber()
        );
    }

    private Specification<OperationEntity> getSpecByFilters(OperationFilters request) {
        return Specification.where(OperationSpecification.userIdEqual(request.getUserId()))
                .and(OperationSpecification.accountIdEquals(request.getAccountId()))
                .and(OperationSpecification.timeStart(request.getTimeStart()))
                .and(OperationSpecification.timeEnd(request.getTimeEnd()))
                .and(OperationSpecification.operationType(request.getOperationType()));
    }
}
