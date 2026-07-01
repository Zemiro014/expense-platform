package com.jeronimo.document_extraction_service.domain.event;

public enum OutboxStatus {
    PENDING,
    PUBLISHED,
    FAILED
}
