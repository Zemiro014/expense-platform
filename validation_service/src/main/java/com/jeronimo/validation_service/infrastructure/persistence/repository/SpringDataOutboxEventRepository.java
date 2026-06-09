package com.jeronimo.validation_service.infrastructure.persistence.repository;

import com.jeronimo.validation_service.domain.event.outbox.OutboxStatus;
import com.jeronimo.validation_service.infrastructure.persistence.entity.outbox.OutboxEventEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataOutboxEventRepository extends JpaRepository<OutboxEventEntity, UUID> {

    List<OutboxEventEntity> findByStatusOrderByCreatedAtAsc(
            OutboxStatus status,
            Pageable pageable
    );
}
