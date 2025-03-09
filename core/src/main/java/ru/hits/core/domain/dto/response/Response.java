package ru.hits.core.domain.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Response {

    private int status;

    private LocalDateTime timestamp;

    private String message;

    public Response(int status, LocalDateTime timestamp, String message) {
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
    }
}
