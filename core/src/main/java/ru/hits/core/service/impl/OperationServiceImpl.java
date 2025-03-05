package ru.hits.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.core.domain.dto.operation.LoanPaymentRequest;
import ru.hits.core.domain.dto.operation.OperationDTO;
import ru.hits.core.domain.dto.operation.OperationRequestBody;
import ru.hits.core.domain.dto.operation.OperationShortDTO;
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

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {

    private final AccountService accountService;
    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;

    private final RpcClientService rpcClientService;

    @Transactional
    @Override
    public OperationDTO createOperation(UUID userId, OperationRequestBody operationRequestBody) throws JsonProcessingException {
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

        var operation = OperationEntity.builder()
                .id(UUID.randomUUID())
                .senderAccountId(operationRequestBody.getSenderAccountId())
                .recipientAccountId(operationRequestBody.getRecipientAccountId())
                .amount(operationRequestBody.getAmount())
                .transactionDateTime(Instant.now())
                .message(operationRequestBody.getMessage())
                .operationType(operationRequestBody.getOperationType())
                .build();

        return operationMapper.entityToDTO(operationRepository.save(operation));
    }

    private OperationDTO createLoanPaymentOperation(UUID userId, OperationRequestBody operationRequestBody) throws JsonProcessingException {
        var senderAccount = accountService.getRawAccount(operationRequestBody.getSenderAccountId());

        if (!senderAccount.getUserId().equals(userId)) {
            throw new ForbiddenException();
        }

        if (senderAccount.getBalance() < operationRequestBody.getAmount()) {
            throw new BadRequestException("Недостаточно средств на счете отправителя.");
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

        return operationMapper.entityToDTO(operationRepository.save(operation));
    }

    private OperationDTO createReplenishmentOperation(OperationRequestBody operationRequestBody) {
        var recipientAccount = accountService.getRawAccount(operationRequestBody.getRecipientAccountId());

        accountService.updateBalance(
                recipientAccount,
                recipientAccount.getBalance() + operationRequestBody.getAmount()
        );

        var operation = OperationEntity.builder()
                .id(UUID.randomUUID())
                .recipientAccountId(operationRequestBody.getRecipientAccountId())
                .amount(operationRequestBody.getAmount())
                .transactionDateTime(Instant.now())
                .message(operationRequestBody.getMessage())
                .operationType(operationRequestBody.getOperationType())
                .build();

        return operationMapper.entityToDTO(operationRepository.save(operation));
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

        return operationMapper.entityToDTO(operationRepository.save(operation));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OperationShortDTO> getOperations(
            UUID userId,
            UUID accountId,
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

        return operationRepository.findAllByAccountId(accountId, pageable)
                .map(operationMapper::entityToShortDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public OperationDTO getOperation(UUID userId, UUID operationId) throws JsonProcessingException {
        var operation = operationMapper.entityToDTO(
                operationRepository.findById(operationId)
                        .orElseThrow(() -> new OperationNotFoundException(operationId))
        );

        var user = rpcClientService.getUserInfo(
                new UserInfoRequest(userId)
        );

        AccountEntity recipientAccount = (operation.getRecipientAccountId() != null)
                ? accountService.getRawAccount(operation.getRecipientAccountId())
                : null;

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

        return operation;
    }
}
