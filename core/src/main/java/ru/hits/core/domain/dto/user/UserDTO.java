package ru.hits.core.domain.dto.user;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserDTO {

    private UUID id;
    private String phone;
    private String name;
    private Boolean isBlocked;
    private List<RoleEnum> roles;

}
