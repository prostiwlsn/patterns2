package ru.hits.core.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleEnum {

    USER(2),
    ADMIN(1),
    MANAGER(0);

    private final Integer value;

    RoleEnum(Integer value) {
        this.value = value;
    }

    @JsonValue
    public Integer getValue() {
        return value;
    }

    @JsonCreator
    public static RoleEnum fromValue(Integer value) {
        for (RoleEnum status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Неизвестное значение: " + value);
    }

}
