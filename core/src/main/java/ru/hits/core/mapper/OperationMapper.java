package ru.hits.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.hits.core.domain.dto.operation.OperationDTO;
import ru.hits.core.domain.entity.OperationEntity;
import ru.hits.core.domain.dto.operation.OperationShortDTO;
import ru.hits.core.domain.enums.OperationTypeEnum;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OperationMapper {

    @Mapping(target = "directionToMe", expression = "java(getMoneyDirection(operationEntity, myAccountIds))")
    @Mapping(target = "senderAccountNumber", source = "senderAccountNumber")
    @Mapping(target = "recipientAccountNumber", source = "recipientAccountNumber")
    @Mapping(target = "amount", expression = "java(getAmountForMe(operationEntity, myAccountIds))")
    OperationDTO entityToDTO(
            OperationEntity operationEntity,
            List<UUID> myAccountIds,
            String senderAccountNumber,
            String recipientAccountNumber
    );

    @Mapping(target = "directionToMe", expression = "java(getMoneyDirection(operationEntity, myAccountIds))")
    OperationShortDTO entityToShortDTO(OperationEntity operationEntity, List<UUID> myAccountIds);

    @Named("moneyDirectionMapper")
    default Boolean getMoneyDirection(OperationEntity operationEntity, List<UUID> myAccountIds) {
        if (operationEntity.getOperationType().equals(OperationTypeEnum.WITHDRAWAL)
                || operationEntity.getOperationType().equals(OperationTypeEnum.LOAN_REPAYMENT)) {
            return false;
        } else if (operationEntity.getOperationType().equals(OperationTypeEnum.REPLENISHMENT)) {
            return true;
        }

        return myAccountIds.stream().noneMatch(uuid -> uuid.equals(operationEntity.getSenderAccountId()));
    }

    @Named("getAmountForMe")
    default Float getAmountForMe(OperationEntity operationEntity, List<UUID> myAccountIds) {
        if (operationEntity.getOperationType().equals(OperationTypeEnum.WITHDRAWAL)
                || operationEntity.getOperationType().equals(OperationTypeEnum.LOAN_REPAYMENT)) {
            return operationEntity.getAmount();
        } else if (operationEntity.getOperationType().equals(OperationTypeEnum.REPLENISHMENT)) {
            return operationEntity.getAmount() * operationEntity.getConversionValue();
        }

        if (myAccountIds.stream().noneMatch(uuid -> uuid.equals(operationEntity.getSenderAccountId()))) {
            return operationEntity.getAmount() * operationEntity.getConversionValue();
        }
        return operationEntity.getAmount();
    }

}
