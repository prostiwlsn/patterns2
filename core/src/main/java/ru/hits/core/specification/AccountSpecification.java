package ru.hits.core.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.hits.core.domain.entity.AccountEntity;

import java.util.UUID;

public final class AccountSpecification {

    public static Specification<AccountEntity> userIdEqual(UUID userId) {
        if (userId == null) {
            return null;
        }

        return ((root, query, cb) -> cb.equal(root.get("userId"), userId));
    }

}
