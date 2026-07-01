package com.jeronimo.document_extraction_service.domain.event;

import com.fasterxml.jackson.databind.JsonNode;
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
    private JsonNode documentExtractedKafkaEvent;
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
            JsonNode documentExtractedKafkaEvent
    ){
        return OutboxEvent.builder()
                .id(UUID.randomUUID())
                .aggregateId(aggregateId)
                .aggregateType(aggregateType)
                .eventType(eventType)
                .topic(topic)
                .documentExtractedKafkaEvent(documentExtractedKafkaEvent)
                .status(OutboxStatus.PENDING)
                .retryCount(0)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
