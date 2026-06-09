package com.jeronimo.validation_service.domain.repository.outbox;

import com.jeronimo.validation_service.domain.event.outbox.OutboxEvent;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository {
    OutboxEvent save(OutboxEvent event);

    List<OutboxEvent> findPending(int limit);

    void markAsPublished(UUID id);

    void markAsFailed(UUID id, String errorMessage);
}
