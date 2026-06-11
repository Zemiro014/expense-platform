package com.jeronimo.document_extraction_service.domain.publisher;

import com.jeronimo.document_extraction_service.domain.event.ReceiptDocumentExtractedEvent;

public interface ReceiptDocumentExtractedEventPublisher {
    void publish(ReceiptDocumentExtractedEvent event);
}
