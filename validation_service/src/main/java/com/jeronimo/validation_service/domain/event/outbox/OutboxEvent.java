package com.jeronimo.validation_service.domain.event.outbox;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class OutboxEvent {
    private UUID id;
    private UUID aggregateId;
    private String aggregateType;
    private String eventType;
    private String topic;
    private String payload;
    private OutboxStatus status;
    private Integer retryCount;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;

    public static OutboxEvent create(
        UUID aggregateId,
        String aggregateType,
        String eventType,
        String topic,
        String payload
    ) {
        return OutboxEvent.builder()
            .id(UUID.randomUUID())
            .aggregateId(aggregateId)
            .aggregateType(aggregateType)
            .eventType(eventType)
            .topic(topic)
            .payload(payload)
            .status(OutboxStatus.PENDING)
            .retryCount(0)
            .createdAt(LocalDateTime.now())
            .build();
    }
}
