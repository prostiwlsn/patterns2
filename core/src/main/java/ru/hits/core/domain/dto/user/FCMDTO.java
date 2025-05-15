package ru.hits.core.domain.dto.user;

import lombok.Builder;
import lombok.Data;
import ru.hits.core.domain.enums.RoleEnum;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class FCMDTO {

    private UUID userId;
    private Boolean isManager;
    private String fcmToken;

}
