package ru.hits.core.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.hits.core.domain.enums.OperationTypeEnum;

@Converter(autoApply = true)
public class OperationTypeEnumConverter implements AttributeConverter<OperationTypeEnum, String> {

    @Override
    public String convertToDatabaseColumn(OperationTypeEnum attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public OperationTypeEnum convertToEntityAttribute(String dbData) {
        return dbData != null ? OperationTypeEnum.fromValue(dbData) : null;
    }
}