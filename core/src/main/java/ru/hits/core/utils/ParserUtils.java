package ru.hits.core.utils;

import ru.hits.core.domain.dto.operation.LoanPaymentErrorResponse;
import ru.hits.core.domain.dto.operation.LoanPaymentSuccessResponse;
import ru.hits.core.domain.enums.RoleEnum;
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

    public static LoanPaymentSuccessResponse parseLoanPaymentSuccessResponse(Map<String, Object> data) {
        var builder = LoanPaymentSuccessResponse.builder();

        if (data.containsKey("SenderAccountId")) {
            builder.senderAccountId(UUID.fromString(data.get("SenderAccountId").toString()));
        }
        if (data.containsKey("RecipientAccountId")) {
            builder.recipientAccountId(UUID.fromString(data.get("RecipientAccountId").toString()));
        }
        if (data.containsKey("ReturnedAmount")) {
            var amount = ((Number) data.get("ReturnedAmount")).floatValue();

            builder.amount(amount);
        }
        if (data.containsKey("IsPaymentExpired")) {
            builder.isPaymentExpired((Boolean) data.get("IsPaymentExpired"));
        }

        return builder.build();
    }

    public static LoanPaymentErrorResponse parseLoanPaymentErrorResponse(Map<String, Object> data) {
        var builder = LoanPaymentErrorResponse.builder();

        if (data.containsKey("SenderAccountId")) {
            builder.senderAccountId(UUID.fromString(data.get("SenderAccountId").toString()));
        }
        if (data.containsKey("ReturnedAmount")) {
            builder.amount((float) data.get("ReturnedAmount"));
        }
        if (data.containsKey("ErrorMessage")) {
            builder.errorMessage(data.get("ErrorMessage").toString());
        }
        if (data.containsKey("ErrorStatusCode")) {
            builder.errorMessage(data.get("ErrorStatusCode").toString());
        }

        return builder.build();
    }

}
