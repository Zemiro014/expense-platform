package com.jeronimo.document_extraction_service.domain.repository;

import com.jeronimo.document_extraction_service.domain.event.OutboxEvent;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository {

    OutboxEvent save(OutboxEvent event);
    List<OutboxEvent> findPending(int limit);
    void markAsPublished(UUID id);
    void markAsFailed(UUID id, String errorMessage);

}
