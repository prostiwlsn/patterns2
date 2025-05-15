package ru.hits.core.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Table(name = "fcm")
@AllArgsConstructor
@NoArgsConstructor
public class FCMEntity {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "is_manager")
    private Boolean isManager;

    @Column(name = "fcm")
    private String fcm;

}
