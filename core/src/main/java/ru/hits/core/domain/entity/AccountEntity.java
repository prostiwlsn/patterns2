package ru.hits.core.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "account")
public class AccountEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "balance", nullable = false)
    private Float balance = 0f;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "create_date_time", nullable = false)
    private Instant createDateTime;

}
