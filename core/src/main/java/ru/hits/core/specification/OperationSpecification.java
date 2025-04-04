package ru.hits.core.specification;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import ru.hits.core.domain.entity.AccountEntity;
import ru.hits.core.domain.entity.OperationEntity;
import ru.hits.core.domain.enums.OperationTypeEnum;

import java.time.Instant;
import java.util.UUID;

public class OperationSpecification {

    public static Specification<OperationEntity> userIdEqual(UUID userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }

            Subquery<UUID> subquery = query.subquery(UUID.class);
            Root<AccountEntity> accountRoot = subquery.from(AccountEntity.class);
            subquery.select(accountRoot.get("id"))
                    .where(criteriaBuilder.equal(accountRoot.get("userId"), userId));

            Predicate senderPredicate = root.get("senderAccountId").in(subquery);
            Predicate recipientPredicate = root.get("recipientAccountId").in(subquery);

            return criteriaBuilder.or(senderPredicate, recipientPredicate);
        };
    }

    public static Specification<OperationEntity> accountIdEquals(UUID accountId) {
        return (root, query, criteriaBuilder) ->
                accountId != null ? criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("senderAccountId"), accountId),
                        criteriaBuilder.equal(root.get("recipientAccountId"), accountId)
                ) : criteriaBuilder.conjunction();
    }

    public static Specification<OperationEntity> isExpired(Boolean isExpired) {
        return (root, query, criteriaBuilder) ->
                isExpired != null ?
                        criteriaBuilder.equal(root.get("isPaymentExpired"), isExpired) : criteriaBuilder.conjunction();
    }

    public static Specification<OperationEntity> loanAccountId(UUID accountId) {
        return (root, query, criteriaBuilder) ->
                accountId != null ?
                        criteriaBuilder.equal(root.get("recipientAccountId"), accountId) : criteriaBuilder.conjunction();
    }

    public static Specification<OperationEntity> timeStart(Instant timeStart) {
        return (root, query, criteriaBuilder) ->
                timeStart != null ? criteriaBuilder.greaterThanOrEqualTo(root.get("transactionDateTime"), timeStart)
                        : criteriaBuilder.conjunction();
    }

    public static Specification<OperationEntity> timeEnd(Instant timeEnd) {
        return (root, query, criteriaBuilder) ->
                timeEnd != null ? criteriaBuilder.lessThanOrEqualTo(root.get("transactionDateTime"), timeEnd)
                        : criteriaBuilder.conjunction();
    }

    public static Specification<OperationEntity> operationType(OperationTypeEnum operationType) {
        return (root, query, criteriaBuilder) ->
                operationType != null ? criteriaBuilder.equal(root.get("operationType"), operationType.getValue())
                        : criteriaBuilder.conjunction();
    }
}
