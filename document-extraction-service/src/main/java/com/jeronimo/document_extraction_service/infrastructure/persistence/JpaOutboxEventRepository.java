package com.jeronimo.document_extraction_service.infrastructure.persistence;

import com.jeronimo.document_extraction_service.domain.event.OutboxEvent;
import com.jeronimo.document_extraction_service.domain.event.OutboxStatus;
import com.jeronimo.document_extraction_service.domain.repository.OutboxEventRepository;
import com.jeronimo.document_extraction_service.infrastructure.persistence.entity.OutboxEventEntity;
import com.jeronimo.document_extraction_service.infrastructure.persistence.mapper.OutboxEventEntityMapper;
import com.jeronimo.document_extraction_service.infrastructure.persistence.repository.SpringDataOutboxEventRepository;
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
