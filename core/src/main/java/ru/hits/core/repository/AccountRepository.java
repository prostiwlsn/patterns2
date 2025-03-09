package ru.hits.core.repository;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hits.core.domain.entity.AccountEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID>, JpaSpecificationExecutor<AccountEntity> {

    @Query(
            value = "select a.id from account a " +
                    "where a.user_id = :userId", nativeQuery = true
    )
    List<UUID> selectAllIdsByUserId(@Param("userId") UUID userId);

    Optional<AccountEntity> findByAccountNumber(String accountNumber);

}
