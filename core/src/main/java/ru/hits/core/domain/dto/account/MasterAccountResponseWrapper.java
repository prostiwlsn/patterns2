package ru.hits.core.domain.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MasterAccountResponseWrapper {
    private boolean success;
    private AccountDTO data;
    private String errorMessage;
}