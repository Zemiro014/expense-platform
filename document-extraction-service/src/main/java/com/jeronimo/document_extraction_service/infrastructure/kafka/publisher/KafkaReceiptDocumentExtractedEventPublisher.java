package com.jeronimo.document_extraction_service.infrastructure.kafka.publisher;

import com.jeronimo.document_extraction_service.domain.event.ReceiptDocumentExtractedEvent;
import com.jeronimo.document_extraction_service.domain.publisher.ReceiptDocumentExtractedEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaReceiptDocumentExtractedEventPublisher implements ReceiptDocumentExtractedEventPublisher {

    private final KafkaTemplate<String, ReceiptDocumentExtractedEvent> kafkaTemplate;

    @Value("${app.kafka.topics.receipt-document-extracted}")
    private String topic;

    @Override
    public void publish(ReceiptDocumentExtractedEvent event) {
        try {
            MDC.put("event", "receipt_document_extracted_publish_requested");
            MDC.put("fileId", event.fileId().toString());
            MDC.put("topic", topic);

            kafkaTemplate.send(topic, event.fileId().toString(), event);

            log.info("Receipt document extracted event published");
        } finally {
            MDC.remove("event");
            MDC.remove("fileId");
            MDC.remove("topic");
        }
    }
}
