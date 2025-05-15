package ru.hits.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hits.core.domain.entity.FCMEntity;

import java.util.List;
import java.util.UUID;

public interface FCMRepository extends JpaRepository<FCMEntity, UUID> {

    List<FCMEntity> findAllByIsManager(Boolean isManager);

}
