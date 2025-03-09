package ru.hits.core.exceptions;

import java.util.UUID;

public class OperationNotFoundException extends RuntimeException {

    public OperationNotFoundException(UUID id) {
        super(String.format("Операция с идентификатором: %s не найдена", id));
    }
}
