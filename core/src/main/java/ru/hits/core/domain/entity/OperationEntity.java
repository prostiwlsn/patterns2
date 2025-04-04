package ru.hits.core.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import ru.hits.core.domain.enums.OperationTypeEnum;
import ru.hits.core.utils.CurrencyEnumConverter;
import ru.hits.core.utils.OperationTypeEnumConverter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "operation")
public class OperationEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "sender_account_id")
    private UUID senderAccountId;

    @Column(name = "recipient_account_id")
    private UUID recipientAccountId;

    @Column(name = "amount", nullable = false)
    private Float amount;

    @Column(name = "conversion_value", nullable = false)
    private Float conversionValue;

    @Column(name = "transaction_date_time", nullable = false)
    private Instant transactionDateTime;

    @Column(name = "message")
    private String message;

    @Convert(converter = OperationTypeEnumConverter.class)
    @Column(name = "operation_type", nullable = false)
    private OperationTypeEnum operationType;

    @Column(name = "is_payment_expired", nullable = true)
    private Boolean isPaymentExpired;

}
