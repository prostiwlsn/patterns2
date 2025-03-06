package ru.hits.core.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.hits.core.domain.enums.OperationTypeEnum;

@Component
public class StringToOperationTypeConverter implements Converter<String, OperationTypeEnum> {
    @Override
    public OperationTypeEnum convert(String source) {
        return OperationTypeEnum.fromValue(source);
    }
}