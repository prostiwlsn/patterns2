package ru.hits.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hits.core.domain.entity.OperationEntity;

import java.util.UUID;

public interface OperationRepository extends JpaRepository<OperationEntity, UUID>, JpaSpecificationExecutor<OperationEntity> {

    @Query(value = "SELECT o FROM OperationEntity o " +
            "WHERE o.senderAccountId = :accountId " +
            "OR o.recipientAccountId = :accountId")
    Page<OperationEntity> findAllByAccountId(@Param("accountId") UUID accountId, Pageable pageable);

}
