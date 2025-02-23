package ru.hits.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.core.domain.dto.operation.OperationDTO;
import ru.hits.core.domain.dto.operation.OperationRequestBody;
import ru.hits.core.domain.dto.operation.OperationShortDTO;
import ru.hits.core.domain.entity.OperationEntity;
import ru.hits.core.domain.enums.OperationTypeEnum;
import ru.hits.core.exceptions.BadRequestException;
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

    @Transactional
    @Override
    public OperationDTO createOperation(OperationRequestBody operationRequestBody) {
        switch (operationRequestBody.getOperationType()) {
            case OperationTypeEnum.TRANSFER -> {
                return createTransferOperation(operationRequestBody);
            }
            case OperationTypeEnum.LOAN_REPAYMENT -> {
                return createLoanPaymentOperation(operationRequestBody);
            }
            case OperationTypeEnum.REPLENISHMENT -> {
                return createReplenishmentOperation(operationRequestBody);
            }
            case OperationTypeEnum.WITHDRAWAL -> {
                return createWithdrawalOperation(operationRequestBody);
            }
            default -> throw new BadRequestException(
                    "Такой операции не существует " + operationRequestBody.getOperationType()
            );
        }
    }

    private OperationDTO createTransferOperation(OperationRequestBody operationRequestBody) {
        var senderAccount = accountService.getRawAccount(operationRequestBody.getSenderAccountId());
        var recipientAccount = accountService.getRawAccount(operationRequestBody.getRecipientAccountId());

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

    private OperationDTO createLoanPaymentOperation(OperationRequestBody operationRequestBody) {
        var senderAccount = accountService.getRawAccount(operationRequestBody.getSenderAccountId());

        if (senderAccount.getBalance() < operationRequestBody.getAmount()) {
            throw new BadRequestException("Недостаточно средств на счете отправителя.");
        }

        accountService.updateBalance(
                senderAccount,
                senderAccount.getBalance() - operationRequestBody.getAmount()
        );

        // TODO обращение к сервису кредитов

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

    private OperationDTO createWithdrawalOperation(OperationRequestBody operationRequestBody) {
        var senderAccount = accountService.getRawAccount(operationRequestBody.getSenderAccountId());

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
    public Page<OperationShortDTO> getOperations(UUID accountId, Pageable pageable) {
        return operationRepository.findAllByAccountId(accountId, pageable)
                .map(operationMapper::entityToShortDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public OperationDTO getOperation(UUID operationId) {
        return operationMapper.entityToDTO(
                operationRepository.findById(operationId)
                        .orElseThrow(() -> new OperationNotFoundException(operationId))
        );
    }
}
