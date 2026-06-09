package com.jeronimo.validation_service.infrastructure.persistence;

import com.jeronimo.validation_service.domain.event.outbox.OutboxEvent;
import com.jeronimo.validation_service.domain.event.outbox.OutboxStatus;
import com.jeronimo.validation_service.domain.repository.outbox.OutboxEventRepository;
import com.jeronimo.validation_service.infrastructure.persistence.entity.outbox.OutboxEventEntity;
import com.jeronimo.validation_service.infrastructure.persistence.mapper.outbox.OutboxEventEntityMapper;
import com.jeronimo.validation_service.infrastructure.persistence.repository.SpringDataOutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaOutboxEventRepository implements OutboxEventRepository {

    private final SpringDataOutboxEventRepository repository;

    @Override
    public OutboxEvent save(OutboxEvent event) {
        OutboxEventEntity saved = repository.save(
                OutboxEventEntityMapper.toEntity(event)
        );

        return OutboxEventEntityMapper.toDomain(saved);
    }

    @Override
    public List<OutboxEvent> findPending(int limit) {
        return repository
            .findByStatusOrderByCreatedAtAsc(
                    OutboxStatus.PENDING,
                    PageRequest.of(0, limit)
            )
            .stream()
            .map(OutboxEventEntityMapper::toDomain)
            .toList();
    }

    @Override
    public void markAsPublished(UUID id) {
        repository.findById(id).ifPresent(entity -> {
            entity.setStatus(OutboxStatus.PUBLISHED);
            entity.setPublishedAt(LocalDateTime.now());
            entity.setErrorMessage(null);
            repository.save(entity);
        });
    }

    @Override
    public void markAsFailed(UUID id, String errorMessage) {
        repository.findById(id).ifPresent(entity -> {
            entity.setStatus(OutboxStatus.FAILED);
            entity.setRetryCount(entity.getRetryCount() + 1);
            entity.setErrorMessage(errorMessage);
            repository.save(entity);
        });
    }
}
