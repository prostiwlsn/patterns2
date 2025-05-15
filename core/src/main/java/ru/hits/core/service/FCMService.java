package ru.hits.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Sort;
import ru.hits.core.domain.dto.account.AccountDTO;
import ru.hits.core.domain.dto.account.AccountFilters;
import ru.hits.core.domain.entity.AccountEntity;
import ru.hits.core.domain.entity.FCMEntity;

import java.util.List;
import java.util.UUID;

public interface FCMService {

    void saveFCMToken(UUID userId, Boolean isManager, String FCMToken);

    FCMEntity getFCMToken(UUID userId);

    List<String> getEmployeeTokens();

}
