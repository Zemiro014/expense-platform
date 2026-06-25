package com.jeronimo.document_extraction_service.infrastructure.kafka.publisher;

import com.fasterxml.jackson.databind.JsonNode;
import com.jeronimo.document_extraction_service.domain.event.OutboxEvent;
import com.jeronimo.document_extraction_service.domain.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxEventPublisher {
    private final OutboxEventRepository outboxRepository;
    private final KafkaTemplate<String, JsonNode> kafkaTemplate;

    public void publish(OutboxEvent event) {
        try {
            MDC.put("event", "outbox_event_publish_requested");
            MDC.put("outboxEventId", event.getId().toString());
            MDC.put("aggregateId", event.getAggregateId().toString());
            MDC.put("topic", event.getTopic());

            log.info("Publishing outbox event to Kafka");
            kafkaTemplate.send(
                    event.getTopic(),
                    event.getAggregateId().toString(),
                    event.getDocumentExtractedKafkaEvent()
            ).get();

            outboxRepository.markAsPublished(event.getId());

            MDC.put("event", "outbox_event_published");
            log.info("Outbox event published successfully");
        } catch (Exception ex) {
            outboxRepository.markAsFailed(
                    event.getId(),
                    ex.getMessage()
            );

            MDC.put("event", "outbox_event_publish_failed");
            log.error("Failed to publish outbox event", ex);

        } finally {
            MDC.remove("event");
            MDC.remove("outboxEventId");
            MDC.remove("aggregateId");
            MDC.remove("topic");
        }
    }
}
