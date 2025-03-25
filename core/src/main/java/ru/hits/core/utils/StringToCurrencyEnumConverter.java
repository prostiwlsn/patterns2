package ru.hits.core.utils;

import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;
import ru.hits.core.domain.dto.currency.CurrencyEnum;

@Component
public class StringToCurrencyEnumConverter implements Converter<String, CurrencyEnum> {
    @Override
    public CurrencyEnum convert(String source) {
        return CurrencyEnum.fromValue(source);
    }
}
