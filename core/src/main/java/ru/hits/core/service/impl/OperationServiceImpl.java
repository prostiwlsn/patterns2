package ru.hits.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.core.domain.dto.currency.CurrencyEnum;
import ru.hits.core.service.FCMService;
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
import ru.hits.core.service.CurrencyService;
import ru.hits.core.service.OperationService;
import ru.hits.core.service.impl.rabbitmq.sender.RabbitMQMessageSender;
import ru.hits.core.service.impl.rabbitmq.rpc.LoanPaymentService;
import ru.hits.core.service.impl.rabbitmq.rpc.UserInfoService;
import ru.hits.core.specification.OperationSpecification;
import ru.hits.core.websocket.OperationsWebSocketHandler;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {

    private final float MAX_MONEY_VALUE = 100000000f;

    private final AccountService accountService;
    private final CurrencyService currencyService;
    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;
    private final OperationsWebSocketHandler operationsWebSocketHandler;
    private final RabbitMQMessageSender rabbitMQMessageSender;
    private final NotificationService notificationService;

    private final UserInfoService userInfoService;
    private final LoanPaymentService loanPaymentService;

    @Override
    public OperationDTO sendCreateOperationMessage(String token, UUID userId, OperationRequestBody operationRequestBody) {
        return rabbitMQMessageSender.sendMessageAndWait(
                new CreateOperationDTO(
                        operationRequestBody.getSenderAccountId(),
                        operationRequestBody.getRecipientAccountId(),
                        operationRequestBody.getAmount(),
                        operationRequestBody.getMessage(),
                        operationRequestBody.getOperationType(),
                        userId,
                        token
                )
        );
    }

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

        var conversationValue = currencyService.getCurrencyValue(
                senderAccount.getCurrency(), recipientAccount.getCurrency()
        );

        accountService.updateBalance(
                senderAccount,
                senderAccount.getBalance() - operationRequestBody.getAmount()
        );
        accountService.updateBalance(
                recipientAccount,
                recipientAccount.getBalance() + (operationRequestBody.getAmount() * conversationValue)
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
                .conversionValue(
                        currencyService.getCurrencyValue(senderAccount.getCurrency(), recipientAccount.getCurrency())
                )
                .build();

        var operationDto = operationMapper.entityToDTO(
                operationRepository.save(operation),
                List.of(operationRequestBody.getSenderAccountId()),
                senderAccount.getAccountNumber(),
                recipientAccount.getAccountNumber()
        );

        operationsWebSocketHandler.sendUpdate(senderAccount.getId().toString(), operationDto.toString());
        operationsWebSocketHandler.sendUpdate(
                recipientAccount.getId().toString(),
                operationMapper.entityToDTO(
                        operationRepository.save(operation),
                        List.of(operationRequestBody.getRecipientAccountId()),
                        recipientAccount.getAccountNumber(),
                        senderAccount.getAccountNumber()
                ).toString()
        );

        notificationService.sendNotifications(operationRequestBody.getOperationType(), senderAccount, recipientAccount, operation.getAmount());

        return operationDto;
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

        var response = loanPaymentService.loanPaymentRequest(
                new LoanPaymentRequest(
                        senderAccount.getId(),
                        operationRequestBody.getRecipientAccountId(),
                        operationRequestBody.getAmount()
                )
        );

        var masterAccount = accountService.getMasterAccount();

        var conversionValue = currencyService.getCurrencyValue(senderAccount.getCurrency(), masterAccount.getCurrency());

        accountService.updateBalance(
                senderAccount,
                senderAccount.getBalance() - operationRequestBody.getAmount()
        );

        createOperation(masterAccount.getUserId(), new OperationRequestBody(
                null,
                masterAccount.getId(),
                operationRequestBody.getAmount() * conversionValue,
                null,
                OperationTypeEnum.REPLENISHMENT
        ));

        var operation = OperationEntity.builder()
                .id(UUID.randomUUID())
                .senderAccountId(operationRequestBody.getSenderAccountId())
                .recipientAccountId(operationRequestBody.getRecipientAccountId())
                .amount(operationRequestBody.getAmount())
                .transactionDateTime(Instant.now())
                .message(operationRequestBody.getMessage())
                .operationType(operationRequestBody.getOperationType())
                .conversionValue(
                        conversionValue
                )
                .isPaymentExpired(response.getIsPaymentExpired())
                .build();

        var operationDto = operationMapper.entityToDTO(
                operationRepository.save(operation),
                List.of(operationRequestBody.getSenderAccountId()),
                senderAccount.getAccountNumber(),
                null
        );

        operationsWebSocketHandler.sendUpdate(senderAccount.getId().toString(), operationDto.toString());

        notificationService.sendNotifications(operationRequestBody.getOperationType(), senderAccount, null, operation.getAmount());

        return operationDto;
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
                .conversionValue(
                        1f
                )
                .build();

        var operationDto = operationMapper.entityToDTO(
                operationRepository.save(operation),
                null,
                null,
                recipientAccount.getAccountNumber()
        );

        operationsWebSocketHandler.sendUpdate(recipientAccount.getId().toString(), operationDto.toString());

        notificationService.sendNotifications(operationRequestBody.getOperationType(), null, recipientAccount, operation.getAmount());

        return operationDto;
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
                .conversionValue(
                        1f
                )
                .build();

        var operationDto = operationMapper.entityToDTO(
                operationRepository.save(operation),
                List.of(operationRequestBody.getSenderAccountId()),
                senderAccount.getAccountNumber(),
                null
        );

        operationsWebSocketHandler.sendUpdate(senderAccount.getId().toString(), operationDto.toString());

        notificationService.sendNotifications(operationRequestBody.getOperationType(), senderAccount, null, operation.getAmount());

        return operationDto;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OperationShortDTO> getOperations(
            UUID userId,
            UUID accountId,
            Instant timeStart,
            Instant timeEnd,
            OperationTypeEnum operationType,
            Pageable pageable,
            String token
    ) throws JsonProcessingException {
        var user = userInfoService.getUserInfo(
                new UserInfoRequest(userId, token)
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

    @Transactional(readOnly = true)
    @Override
    public Page<OperationShortDTO> getExpiredOperations(
            UUID myUserId,
            UUID userId,
            UUID loanAccountId,
            Pageable pageable,
            String token
    ) throws JsonProcessingException {
        if (userId == null) {
            userId = myUserId;
        }
        var user = userInfoService.getUserInfo(
                new UserInfoRequest(myUserId, token)
        );

        if (
                user.getRoles().stream().noneMatch(r -> r.equals(RoleEnum.ADMIN) || r.equals(RoleEnum.MANAGER))
                        && !user.getId().equals(userId)
        ) {
            throw new ForbiddenException();
        }

        var accounts = accountService.getMyAccountIds(userId);

        return getOperations(
                OperationFilters.builder()
                        .userId(userId)
                        .isExpired(true)
                        .loanAccountId(loanAccountId)
                        .build(),
                pageable,
                accounts
        );
    }

    private Page<OperationShortDTO> getOperations(OperationFilters filters, Pageable pageable, List<UUID> accountIds) {
        var spec = getSpecByFilters(filters);
        return operationRepository.findAll(spec, pageable).map(o -> operationMapper.entityToShortDTO(o, accountIds));
    }

    @Transactional(readOnly = true)
    @Override
    public OperationDTO getOperation(
            UUID userId,
            UUID accountId,
            UUID operationId,
            String token
    ) throws JsonProcessingException {
        List<UUID> accountIds = List.of(accountId);

        OperationEntity operation = operationRepository.findById(operationId)
                .orElseThrow(() -> new OperationNotFoundException(operationId));

        var user = userInfoService.getUserInfo(
                new UserInfoRequest(userId, token)
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

    @Override
    @SneakyThrows
    @Transactional
    public void masterAccountWithdrawal(Float amount) {
        var masterAccount = accountService.getMasterAccount();

        createOperation(masterAccount.getUserId(), new OperationRequestBody(
                masterAccount.getId(),
                null,
                amount,
                null,
                OperationTypeEnum.WITHDRAWAL
        ));
    }

    private Specification<OperationEntity> getSpecByFilters(OperationFilters request) {
        return Specification.where(OperationSpecification.userIdEqual(request.getUserId()))
                .and(OperationSpecification.accountIdEquals(request.getAccountId()))
                .and(OperationSpecification.timeStart(request.getTimeStart()))
                .and(OperationSpecification.timeEnd(request.getTimeEnd()))
                .and(OperationSpecification.operationType(request.getOperationType()))
                .and(OperationSpecification.isExpired(request.getIsExpired()))
                .and(OperationSpecification.loanAccountId(request.getLoanAccountId()));
    }
}
