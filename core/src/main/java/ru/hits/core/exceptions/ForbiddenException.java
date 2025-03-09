package ru.hits.core.exceptions;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        super("У вас нет прав доступа");
    }
}
