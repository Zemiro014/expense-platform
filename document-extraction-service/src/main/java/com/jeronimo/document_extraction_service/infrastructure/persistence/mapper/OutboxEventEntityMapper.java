package com.jeronimo.document_extraction_service.infrastructure.persistence.mapper;

import com.jeronimo.document_extraction_service.domain.event.OutboxEvent;
import com.jeronimo.document_extraction_service.infrastructure.persistence.entity.OutboxEventEntity;

public class OutboxEventEntityMapper {
    private OutboxEventEntityMapper() {
    }

    public static OutboxEventEntity toEntity(OutboxEvent event) {
        OutboxEventEntity entity = new OutboxEventEntity();

        entity.setId(event.getId());
        entity.setAggregateId(event.getAggregateId());
        entity.setAggregateType(event.getAggregateType());
        entity.setEventType(event.getEventType());
        entity.setTopic(event.getTopic());
        entity.setPayload(event.getPayload());
        entity.setStatus(event.getStatus());
        entity.setRetryCount(event.getRetryCount());
        entity.setErrorMessage(event.getErrorMessage());
        entity.setCreatedAt(event.getCreatedAt());
        entity.setPublishedAt(event.getPublishedAt());

        return entity;
    }

    public static OutboxEvent toDomain(OutboxEventEntity entity) {
        return OutboxEvent.builder()
                .id(entity.getId())
                .aggregateId(entity.getAggregateId())
                .aggregateType(entity.getAggregateType())
                .eventType(entity.getEventType())
                .topic(entity.getTopic())
                .payload(entity.getPayload())
                .status(entity.getStatus())
                .retryCount(entity.getRetryCount())
                .errorMessage(entity.getErrorMessage())
                .createdAt(entity.getCreatedAt())
                .publishedAt(entity.getPublishedAt())
                .build();
    }
}
