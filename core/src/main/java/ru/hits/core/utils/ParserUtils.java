package ru.hits.core.utils;

import ru.hits.core.domain.dto.user.RoleEnum;
import ru.hits.core.domain.dto.user.UserDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ParserUtils {

    public static UserDTO parseUserDto(Map<String, Object> data) {
        UserDTO.UserDTOBuilder builder = UserDTO.builder();

        if (data.containsKey("Id")) {
            builder.id(UUID.fromString(data.get("Id").toString()));
        }
        if (data.containsKey("Phone")) {
            builder.phone(data.get("Phone").toString());
        }
        if (data.containsKey("Name")) {
            builder.name(data.get("Name").toString());
        }
        if (data.containsKey("IsBlocked")) {
            builder.isBlocked(Boolean.parseBoolean(data.get("IsBlocked").toString()));
        }
        if (data.containsKey("Roles")) {
            List<RoleEnum> roles = ((List<Integer>) data.get("Roles"))
                    .stream().map(RoleEnum::fromValue).collect(Collectors.toList());

            builder.roles(roles);
        }

        return builder.build();
    }

}
