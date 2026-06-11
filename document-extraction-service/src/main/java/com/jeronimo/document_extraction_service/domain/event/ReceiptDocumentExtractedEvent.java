package com.jeronimo.document_extraction_service.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReceiptDocumentExtractedEvent(
    UUID eventId,
    Integer eventVersion,
    UUID fileId,
    String originalFilename,
    String contentType,
    String extractedText,
    String correlationId,
    LocalDateTime extractedAt
) { }
