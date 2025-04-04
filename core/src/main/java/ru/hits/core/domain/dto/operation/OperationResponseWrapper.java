package ru.hits.core.domain.dto.operation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationResponseWrapper {
    private boolean success;
    private OperationDTO data;
    private String errorMessage;
}