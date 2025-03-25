package ru.hits.core.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.hits.core.domain.dto.currency.CurrencyEnum;

@Converter(autoApply = true)
public class CurrencyEnumConverter implements AttributeConverter<CurrencyEnum, String> {

    @Override
    public String convertToDatabaseColumn(CurrencyEnum attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public CurrencyEnum convertToEntityAttribute(String dbData) {
        return dbData != null ? CurrencyEnum.fromValue(dbData) : null;
    }
}
