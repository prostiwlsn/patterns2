package ru.hits.core.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OperationTypeEnum {

    REPLENISHMENT("replenishment"),
    WITHDRAWAL("withdrawal"),
    TRANSFER("transfer"),
    LOAN_REPAYMENT("loan_payment");

    private final String value;

    OperationTypeEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static OperationTypeEnum fromValue(String value) {
        for (OperationTypeEnum status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

}
